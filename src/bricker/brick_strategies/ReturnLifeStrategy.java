package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraLife;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * A collision strategy that breaks the brick and drops a heart from its center.
 * Catching the heart with the original paddle returns a life to the player.
 */
public class ReturnLifeStrategy extends BasicCollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Renderable heartImage;
    private final Vector2 heartSize;
    private final GameObject originalPaddle;
    private final Counter livesCounter;
    private final int maxLives;
    private final float windowHeight;


    /**
     * Constructs a return life strategy.
     *
     * @param gameObjects    The game's object collection.
     * @param bricksCounter  Counter of the bricks remaining in the game.
     * @param heartImage     The image of a heart.
     * @param heartSize      The size of a heart.
     * @param originalPaddle The only paddle that can catch the heart.
     * @param livesCounter   Counter of the player's lives.
     * @param maxLives       The maximal number of lives.
     * @param windowHeight   The height of the game window.
     */
    public ReturnLifeStrategy(GameObjectCollection gameObjects,
                              Counter bricksCounter,
                              Renderable heartImage,
                              Vector2 heartSize,
                              GameObject originalPaddle,
                              Counter livesCounter,
                              int maxLives,
                              float windowHeight) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.heartImage = heartImage;
        this.heartSize = heartSize;
        this.originalPaddle = originalPaddle;
        this.livesCounter = livesCounter;
        this.maxLives = maxLives;
        this.windowHeight = windowHeight;
    }


    /**
     * Drops a heart from the center of the broken brick.
     *
     * @param brick The brick that was broken.
     * @param other The object that broke the brick.
     */
    @Override
    protected void onBrickBroken(Brick brick, GameObject other) {
        ExtraLife extraLife = new ExtraLife(
                Vector2.ZERO,
                heartSize,
                heartImage,
                originalPaddle,
                livesCounter,
                maxLives,
                gameObjects,
                windowHeight);
        extraLife.setCenter(brick.getCenter());
        gameObjects.addGameObject(extraLife);
    }
}