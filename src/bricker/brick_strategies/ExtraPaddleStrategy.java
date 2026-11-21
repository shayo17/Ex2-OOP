package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * A collision strategy that breaks the brick and adds an extra paddle in the
 * middle of the screen, if there isn't one already.
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy {
    private static final float CENTER_FACTOR = 0.5f;
    private final GameObjectCollection gameObjects;
    private final Renderable paddleImage;
    private final Vector2 paddleSize;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final Counter activeExtraPaddles;


    /**
     * Constructs an extra paddle strategy.
     *
     * @param gameObjects        The game's object collection.
     * @param bricksCounter      Counter of the bricks remaining in the game.
     * @param paddleImage        The image of the paddle.
     * @param paddleSize         The size of the paddle.
     * @param inputListener      Used to read the player's keys.
     * @param windowDimensions   The dimensions of the game window.
     * @param activeExtraPaddles Counter of extra paddles currently in the game,
     *                           shared by all extra paddle strategies.
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects,
                               Counter bricksCounter,
                               Renderable paddleImage,
                               Vector2 paddleSize,
                               UserInputListener inputListener,
                               Vector2 windowDimensions,
                               Counter activeExtraPaddles) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.paddleImage = paddleImage;
        this.paddleSize = paddleSize;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.activeExtraPaddles = activeExtraPaddles;
    }


    /**
     * Adds an extra paddle in the center of the window, unless an extra paddle
     * already exists in the game.
     *
     * @param brick The brick that was broken.
     * @param other The object that broke the brick.
     */
    @Override
    protected void onBrickBroken(Brick brick, GameObject other) {
        if (activeExtraPaddles.value() <= 0) {
            ExtraPaddle extraPaddle = new ExtraPaddle(
                    Vector2.ZERO,
                    paddleSize,
                    paddleImage,
                    inputListener,
                    windowDimensions,
                    gameObjects,
                    activeExtraPaddles);
            extraPaddle.setCenter(windowDimensions.mult(CENTER_FACTOR));
            gameObjects.addGameObject(extraPaddle);
            activeExtraPaddles.increment();
        }
    }
}