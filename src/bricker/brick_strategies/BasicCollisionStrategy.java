package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;

    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter bricksCounter) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
    }

    @Override
    public void onCollision(Brick brick, GameObject other) {
        System.out.println("collision with brick detected");
        gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS);
        bricksCounter.decrement();
    }
}