package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;


/**
 * The basic collision strategy: when a brick is hit, it is removed from the game
 * and the bricks counter is decremented. Special strategies extend this class and
 * override {@link #onBrickBroken(Brick, GameObject)} to add their own behavior.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;


    /**
     * Constructs a basic collision strategy.
     *
     * @param gameObjects   The game's object collection, to remove the brick from.
     * @param bricksCounter Counter of the bricks remaining in the game.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }


    /**
     * Removes the brick from the game. If the brick was actually removed (that is,
     * it was not already broken), decrements the bricks counter and triggers
     * {@link #onBrickBroken(Brick, GameObject)}.
     *
     * @param brick The brick that was hit.
     * @param other The object that hit the brick.
     */
    @Override
    public void onCollision(Brick brick, GameObject other) {
        if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
            bricksCounter.decrement();
            onBrickBroken(brick, other);
        }
    }


    /**
     * Called exactly once, right after the brick was removed from the game.
     * Does nothing by default; special strategies override it to add their behavior.
     *
     * @param brick The brick that was broken.
     * @param other The object that broke the brick.
     */
    protected void onBrickBroken(Brick brick, GameObject other) {}
}