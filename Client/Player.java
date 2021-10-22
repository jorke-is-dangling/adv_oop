package nl.rug.aoop.asteroids.Networking.Client;

import nl.rug.aoop.asteroids.Networking.ConnectionDetails;
import nl.rug.aoop.asteroids.Networking.PacketHandler;
import nl.rug.aoop.asteroids.control.actions.NewGameAction;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.model.MultiPlayerGame;
import nl.rug.aoop.asteroids.view.gameview.AsteroidsFrame;
import nl.rug.aoop.asteroids.view.menuview.MainMenuFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Threaded class of user that joins a hosted game and controls it by pressing keys
 */
public class Player extends PacketHandler implements Runnable, KeyListener {
    private String userType;
    private boolean running;
    private MultiPlayerGame game;
    private final DatagramSocket datagramSocket;

    public Player(String userType) throws IOException {
        datagramSocket = new DatagramSocket();
        this.running = false;
        this.userType = userType;
        this.game = null;
    }

    private int getPort(){
        String portString = JOptionPane.showInputDialog("Private server port");
        return Integer.parseInt(portString);
    }

    private Game initialiseConnection() throws IOException{
        int port = getPort();
        var connectionDetails = new ConnectionDetails(InetAddress.getLocalHost(), port);
        send(datagramSocket,0,connectionDetails);
        return receiveGame(datagramSocket);

    }

    private void initGUI(){
        AsteroidsFrame frame = new AsteroidsFrame(game);
        // Generate a new action event so that we can use the NewGameAction to start a new game.
        new NewGameAction(game).actionPerformed(
                // Just use a dummy action; NewGameAction doesn't care about the action event's properties.
                new ActionEvent(frame, ActionEvent.ACTION_PERFORMED, null)
        );
    }

    @Override
    public void run() {
        // 1. Establish connection
        try {
            this.game = initialiseConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 2. Setup GUI
        initGUI();

        // 3. Interact with server
        running = true;
        while(running){
            try {
                int input = recieveInt(datagramSocket);
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }

        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        //TODO: change them to send packet

    }

    @Override
    public void keyReleased(KeyEvent event) {
        //TODO: change them to send packet

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}