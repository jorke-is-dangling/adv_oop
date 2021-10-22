package nl.rug.aoop.asteroids.Networking.Client;

import nl.rug.aoop.asteroids.Networking.PacketHandler;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyHandler implements KeyListener {
    /**
     * The key that, when pressed, causes the ship to accelerate.
     */
    private static final int ACCELERATION_KEY = KeyEvent.VK_W;

    /**
     * The key that turns the ship left, or counter-clockwise.
     */
    private static final int LEFT_KEY = KeyEvent.VK_A;

    /**
     * The key that turns the ship right, or clockwise.
     */
    private static final int RIGHT_KEY = KeyEvent.VK_D;

    /**
     * The key that causes the ship to fire its weapon.
     */
    private static final int FIRE_WEAPON_KEY = KeyEvent.VK_SPACE;

    /**
     * The spaceship that will respond to key events caught by this listener.
     */
    private final Player player;

    /**
     * Constructs a new player key listener to control the given ship.
     *
     * @param player
     */
    public PlayerKeyHandler(Player player) {
        this.player = player;
    }

    /**
     * This method is invoked when a key is pressed and sets the corresponding fields in the spaceship to true.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        player.send();
    }

    /**
     * This method is invoked when a key is released and sets the corresponding fields in the spaceship to false.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case ACCELERATION_KEY -> ship.setAccelerateKeyPressed(false);
            case LEFT_KEY -> ship.setTurnLeftKeyPressed(false);
            case RIGHT_KEY -> ship.setTurnRightKeyPressed(false);
            case FIRE_WEAPON_KEY -> ship.setFiring(false);
        }
    }

    /**
     * This method doesn't do anything, but we must provide an empty implementation to satisfy the contract of the
     * KeyListener interface.
     *
     * @param event Key event that triggered the method.
     */
    @Override
    public void keyTyped(KeyEvent event) {
    }
}
