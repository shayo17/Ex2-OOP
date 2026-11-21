package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * A ball that bounces off other objects and plays a sound on every collision.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;


    /**
     * Constructs a ball.
     *
     * @param topLeftCorner  Position of the ball, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height of the ball, in window coordinates.
     * @param renderable     The renderable representing the ball.
     * @param collisionSound The sound played when the ball collides with an object.
     */
    public Ball(Vector2 topLeftCorner,
                Vector2 dimensions,
                Renderable renderable,
                Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }


    /**
     * Bounces the ball off the object it collided with, and plays the collision sound.
     *
     * @param other     The object the ball collided with.
     * @param collision Information about the collision, including its normal.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }
}