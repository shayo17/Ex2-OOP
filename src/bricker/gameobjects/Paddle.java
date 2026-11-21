package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;


/**
 * The player's paddle. Moves left and right according to the arrow keys,
 * and stays between the walls of the window.
 */
public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final float WALL_WIDTH = 3;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;


    /**
     * Constructs a paddle.
     *
     * @param topLeftCorner    Position of the paddle, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height of the paddle, in window coordinates.
     * @param renderable       The renderable representing the paddle.
     * @param inputListener    Used to read the arrow keys pressed by the player.
     * @param windowDimensions The dimensions of the game window, used to keep the
     *                         paddle inside the window.
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener,
                  Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }


    /**
     * Keeps the paddle inside the window, and sets its velocity according to the
     * arrow keys currently pressed.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 leftCorner = this.getTopLeftCorner();
        if (leftCorner.x() + getDimensions().x() >= windowDimensions.x() - WALL_WIDTH) {
            this.setTopLeftCorner(new Vector2(windowDimensions.x() - WALL_WIDTH - getDimensions().x(), leftCorner.y()));
        }
        if (leftCorner.x() <= WALL_WIDTH) {
            this.setTopLeftCorner(new Vector2(WALL_WIDTH, leftCorner.y()));
        }
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}