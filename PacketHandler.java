package nl.rug.aoop.asteroids.Networking;

import nl.rug.aoop.asteroids.model.Game;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class PacketHandler {
    public static final int BUFFER_SIZE = 4;
    public static final int MAX_SIZE = 1024;

    /*
    Generic sena dn receive methods
     */

    /**
     * Client sends key value to server
     * @param datagramSocket socket connected to server
     * @param data byte array to be sent in packet
     * @param connectionDetails port and ip addres
     * @throws IOException in sending packet
     */
    public void send(DatagramSocket datagramSocket, byte[] data, ConnectionDetails connectionDetails) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, connectionDetails.ipAddress(),
                connectionDetails.port());
        datagramSocket.send(datagramPacket);
    }

    /**
     * Server eceive packet from client
     * @param datagramSocket socket connected to client
     * @return Packet receieved
     * @throws IOException in receiving packet
     */
    public DatagramPacket receive(DatagramSocket datagramSocket) throws IOException{
        byte[] data = new byte[MAX_SIZE];

        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        datagramSocket.receive(receivePacket);

        return receivePacket;
    }

    /*
    Section of PacketHandler involving sending and receiving state of Game class
     */

    /**
     *
     * @param game Turn game object to compressed byte array
     * @return byte array of game
     * @throws IOException in writing game object
     */
    public byte[] getCompressedByteData(Game game) throws  IOException{
        try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            GZIPOutputStream gzipOut = new GZIPOutputStream(byteOut);
            ObjectOutputStream objOut = new ObjectOutputStream(gzipOut)){
            objOut.writeObject(game);
            gzipOut.close();
            return byteOut.toByteArray();
        }
    }

    /**
     *
     * @param game Turn game object to umcompressed byte array
     * @return byte array of game
     * @throws IOException in writing game object
     */
    public byte[] getUnCompressedByteData(Game game) throws IOException{
        try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut)){
            objOut.writeObject(game);
            return byteOut.toByteArray();
        }
    }

    /**
     * turn byte array to Game file
     * @param data byte array of game received
     * @return
     * @throws IOException
     */
    public Game readCompressedByteData(byte[] data) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            GZIPInputStream gzipIn = new GZIPInputStream(byteIn);
            ObjectInputStream objIn = new ObjectInputStream(gzipIn)){
            return (Game) objIn.readObject();
        }
    }

    /**
     * Client reads uncompressed game file from server
     * @param data
     * @return
     * @throws IOException
     */
    public Game readUncompressedByteData(byte[] data) throws IOException{
        try(ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
            ObjectInputStream objIn = new ObjectInputStream(byteIn)){
            return (Game) objIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Packet received by server is turned into game
     * @param datagramSocket socket connected to client
     * @return Received int
     * @throws IOException in receiving packet
     */
    public Game receiveGame(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket datagramPacket = receive(datagramSocket);
        return
    }

    /*
    Section of PacketHandler.java involving sending and receiving only integer
     */

    /**
     * Turne integer to byte array
     * @param value integer to be turned to byte array
     * @return byte array of integer
     */
    public byte[] getByteData(int value) {
        ByteBuffer b = ByteBuffer.allocate(BUFFER_SIZE);
        b.putInt(value);
        return b.array();
    }


    /**
     * Packet received by server is turned into integer
     * @param datagramSocket socket connected to client
     * @return Received int
     * @throws IOException in receiving packet
     */
    public int receiveInt(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket datagramPacket = receive(datagramSocket);
        ByteBuffer wrapped = ByteBuffer.wrap(datagramPacket.getData());

        return wrapped.getInt();
    }
}
