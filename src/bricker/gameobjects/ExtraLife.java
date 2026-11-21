package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * A heart that falls from a broken brick. If caught by the original paddle,
 * it returns a life to the player (up to the maximal number of lives).
 */
public class ExtraLife extends GameObject {

    private static final float FALL_SPEED = 100;

    private final GameObject originalPaddle;
    private final Counter livesCounter;
    private final int maxLives;
    private final GameObjectCollection gameObjects;
    private final float windowHeight;


    /**
     * Constructs a falling extra life.
     *
     * @param topLeftCorner  Position of the heart, in window coordinates (pixels).
     * @param dimensions     Width and height of the heart.
     * @param renderable     The image of the heart.
     * @param originalPaddle The only paddle that can catch the heart.
     * @param livesCounter   Counter of the player's lives.
     * @param maxLives       The maximal number of lives.
     * @param gameObjects    The game's object collection, to remove the heart from.
     * @param windowHeight   The height of the game window.
     */
    public ExtraLife(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                     GameObject originalPaddle,
                     Counter livesCounter, int maxLives,
                     GameObjectCollection gameObjects,
                     float windowHeight) {
        super(topLeftCorner, dimensions, renderable);
        this.originalPaddle = originalPaddle;
        this.livesCounter = livesCounter;
        this.maxLives = maxLives;
        this.gameObjects = gameObjects;
        this.windowHeight = windowHeight;
        setVelocity(new Vector2(0, FALL_SPEED));
    }


    /**
     * The heart collides only with the original paddle, and passes through
     * every other object.
     *
     * @param other The object to check.
     * @return true if other is the original paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == originalPaddle;
    }


    /**
     * Called when the heart is caught by the original paddle. Adds a life if the
     * player has less than the maximal number of lives, and removes the heart.
     *
     * @param other     The object the heart collided with (the original paddle).
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (livesCounter.value() < maxLives) {
            livesCounter.increment();
        }
        this.gameObjects.removeGameObject(this);
    }


    /**
     * Moves the heart, and removes it once it has fallen below the window.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y() > windowHeight) {
            gameObjects.removeGameObject(this);
        }
    }
}