package nl.rug.aoop.asteroids.Networking.Server;

import nl.rug.aoop.asteroids.Networking.ConnectionDetails;
import nl.rug.aoop.asteroids.Networking.PacketHandler;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.model.MultiPlayerGame;
import nl.rug.aoop.asteroids.view.menuview.MainMenuFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer extends PacketHandler implements Runnable{
    private int threadNr;
    private final ExecutorService executorService;
    private boolean running;
    private MultiPlayerGame game;

    private GameServer(){
        this.game = new MultiPlayerGame();
        this.threadNr = 1;
        this.running = false;
        executorService = Executors.newCachedThreadPool();
    }

    private ConnectionDetails connect(DatagramSocket datagramSocket) throws IOException {
        DatagramPacket initialPacket = receive(datagramSocket);
        return new ConnectionDetails(initialPacket.getAddress(), initialPacket.getPort());
    }

    private void handleIncomingRequests(DatagramSocket datagramSocket){
        try{
            ConnectionDetails connectionDetails = connect(datagramSocket);
            //Spawning thread
            executorService.submit(new ClientHandler(connectionDetails,threadNr, game));
            threadNr ++;
        } catch (IOException e) {
            //TODO: logging
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try(DatagramSocket datagramSocket = new DatagramSocket(0)){
            running = true;
            while(running){
                handleIncomingRequests(datagramSocket);
            }
        } catch(SocketException e){
            //TODO: logging
            e.printStackTrace();
        }
    }
}
