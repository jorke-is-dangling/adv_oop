package nl.rug.aoop.asteroids.Networking.Server;

import nl.rug.aoop.asteroids.Networking.ConnectionDetails;
import nl.rug.aoop.asteroids.Networking.PacketHandler;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.model.MultiPlayerGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramSocket;

public class ClientHandler extends PacketHandler implements Runnable {
    public static final int SLEEP_TIME = 100;
    private final DatagramSocket datagramSocket;
    private final ConnectionDetails connectionDetails;
    private final int threadNr;
    private boolean running;
    private MultiPlayerGame game;

    /**
     * Constructs a new player key listener to send datagram to server
     *
     */
    public ClientHandler(ConnectionDetails connectionDetails, int threadNr, MuliPlayerGame game) throws IOException {
        datagramSocket = new DatagramSocket();
        this.connectionDetails = connectionDetails;
        this.threadNr = threadNr;
        this.running = false;
        this.game = game;
    }

    @Override
    public void run() {
        //TODO: send empty packet
        running = true;
        while(running) {
            try{
                byte[] data = getCompressedByteData(game);
                send(datagramSocket, data , connectionDetails);
                Thread.sleep(100);
            }catch(IOException e){
                //TODO: logging
                e.printStackTrace();
            }catch (InterruptedException e){
                //TODO: logging
                e.printStackTrace();
            }
        }
    }


}
