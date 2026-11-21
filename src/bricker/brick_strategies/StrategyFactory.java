package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Creates collision strategies for bricks, drawn at random according to the
 * game's distribution: 1/2 basic, and 1/8 for each of the four special behaviors.
 */
public class StrategyFactory {
    private static final int NUM_OF_OPTIONS = 8;
    private static final int EXTRA_BALLS = 4;
    private static final int EXTRA_PADDLE = 5;
    private static final int EXPLODING = 6;
    private static final int RETURN_LIFE = 7;
    private static final float PUCK_SIZE_FACTOR = 0.75f;
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String EXPLOSION_SOUND_PATH = "assets/explosion.wav";

    private final Random random = new Random();
    private final GameObjectCollection gameObjects;
    private final Counter bricksCounter;
    private final Counter livesCounter;
    private final Counter activeExtraPaddles;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final GameObject originalPaddle;
    private final Brick[][] bricks;
    private final Vector2 puckSize;
    private final Vector2 paddleSize;
    private final Vector2 heartSize;
    private final float ballSpeed;
    private final int maxLives;
    private final Renderable puckImage;
    private final Renderable paddleImage;
    private final Renderable heartImage;
    private final Sound ballSound;
    private final Sound explosionSound;

    /**
     * Constructs a strategy factory.
     *
     * @param gameObjects      The game's object collection.
     * @param bricksCounter    Counter of the bricks remaining in the game.
     * @param livesCounter     Counter of the player's lives.
     * @param imageReader      Used to read the images of the special objects.
     * @param soundReader      Used to read the sounds of the special objects.
     * @param inputListener    Used by the extra paddle to read the player's keys.
     * @param windowDimensions The dimensions of the game window.
     * @param originalPaddle   The player's original paddle.
     * @param bricks           The grid of bricks, indexed by [row][col].
     * @param ballSize         The size of the main ball.
     * @param paddleSize       The size of the paddle.
     * @param heartSize        The size of a heart in the lives display.
     * @param ballSpeed        The speed of the main ball.
     * @param maxLives         The maximal number of lives.
     */
    public StrategyFactory(GameObjectCollection gameObjects,
                           Counter bricksCounter,
                           Counter livesCounter,
                           ImageReader imageReader,
                           SoundReader soundReader,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           GameObject originalPaddle,
                           Brick[][] bricks,
                           Vector2 ballSize,
                           Vector2 paddleSize,
                           Vector2 heartSize,
                           float ballSpeed,
                           int maxLives) {
        this.gameObjects = gameObjects;
        this.bricksCounter = bricksCounter;
        this.livesCounter = livesCounter;
        this.activeExtraPaddles = new Counter(0);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.originalPaddle = originalPaddle;
        this.bricks = bricks;
        this.puckSize = ballSize.mult(PUCK_SIZE_FACTOR);
        this.paddleSize = paddleSize;
        this.heartSize = heartSize;
        this.ballSpeed = ballSpeed;
        this.maxLives = maxLives;
        this.puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        this.paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        this.heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        this.ballSound = soundReader.readSound(BALL_SOUND_PATH);
        this.explosionSound = soundReader.readSound(EXPLOSION_SOUND_PATH);
    }

    /**
     * Draws a collision strategy at random according to the game's distribution.
     *
     * @return A new collision strategy.
     */
    public CollisionStrategy createRandomStrategy() {
        switch (random.nextInt(NUM_OF_OPTIONS)) {
            case EXTRA_BALLS:
                return new ExtraBallsStrategy(gameObjects, bricksCounter, puckImage, ballSound,
                        puckSize, ballSpeed, windowDimensions.y());
            case EXTRA_PADDLE:
                return new ExtraPaddleStrategy(gameObjects, bricksCounter, paddleImage, paddleSize,
                        inputListener, windowDimensions, activeExtraPaddles);
            case EXPLODING:
                return new ExplodingStrategy(gameObjects, bricksCounter, bricks, explosionSound);
            case RETURN_LIFE:
                return new ReturnLifeStrategy(gameObjects, bricksCounter, heartImage, heartSize,
                        originalPaddle, livesCounter, maxLives, windowDimensions.y());
            default:
                return new BasicCollisionStrategy(gameObjects, bricksCounter);
        }
    }
}