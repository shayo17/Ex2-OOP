package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * An extra paddle that moves together with the player's arrow keys, like the
 * original paddle. It disappears after being hit a fixed number of times by
 * balls or pucks.
 */
public class ExtraPaddle extends Paddle {

    private static final int MAX_COLLISIONS = 4;
    private int collisionCount = 0;
    private final GameObjectCollection gameObjects;
    private final Counter activeExtraPaddles;


    /**
     * Constructs an extra paddle.
     *
     * @param topLeftCorner      Position of the paddle, in window coordinates (pixels).
     *                           Note that (0,0) is the top-left corner of the window.
     * @param dimensions         Width and height of the paddle, in window coordinates.
     * @param renderable         The renderable representing the paddle.
     * @param inputListener      Used to read the arrow keys pressed by the player.
     * @param windowDimensions   The dimensions of the game window.
     * @param gameObjects        The game's object collection, to remove the paddle from.
     * @param activeExtraPaddles Counter of extra paddles currently in the game,
     *                           decremented when this paddle disappears.
     */
    public ExtraPaddle(Vector2 topLeftCorner,
                       Vector2 dimensions,
                       Renderable renderable,
                       UserInputListener inputListener,
                       Vector2 windowDimensions,
                       GameObjectCollection gameObjects,
                       Counter activeExtraPaddles) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.gameObjects = gameObjects;
        this.activeExtraPaddles = activeExtraPaddles;
    }


    /**
     * Counts the hits on the paddle. After the maximal number of hits, the paddle
     * removes itself from the game and updates the active extra paddles counter.
     *
     * @param other     The object that hit the paddle.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionCount++;
        if (collisionCount >= MAX_COLLISIONS && gameObjects.removeGameObject(this)) {
            activeExtraPaddles.decrement();
        }
    }
}