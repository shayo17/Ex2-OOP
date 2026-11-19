package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;

    public BasicCollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    @Override
    public void onCollision(Brick brick, GameObject other) {
        System.out.println("collision with brick detected");
        gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS);
    }
}