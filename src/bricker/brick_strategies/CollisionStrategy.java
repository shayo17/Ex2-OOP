package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;


/**
 * Defines what happens when a brick is hit. Each brick holds a collision strategy
 * and delegates to it on collision, so a brick's behavior can be changed without
 * changing the Brick class itself.
 */
public interface CollisionStrategy {


    /**
     * Called when a brick is hit by another game object.
     *
     * @param brick The brick that was hit.
     * @param other The object that hit the brick.
     */
    void onCollision(Brick brick, GameObject other);
}
