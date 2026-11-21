package bricker.main;

import bricker.brick_strategies.StrategyFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;


/**
 * The manager of the Bricker game. Creates all the game objects (background,
 * walls, ball, paddle, bricks and lives displays), gives each brick a randomly
 * drawn collision strategy, and checks every frame whether the player has won
 * or lost.
 */
public class BrickerGameManager extends GameManager {

    private static final int BALL_SPEED = 200;
    private static final int WALL_WIDTH = 3;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_BOTTOM_OFFSET = 30;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int DEFAULT_NUM_OF_ROWS = 7;
    private static final int DEFAULT_NUM_OF_BRICKS = 8;
    private static final int NUM_OF_USER_INPUT = 2;
    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_SPACING = 2;
    private static final int PLAYER_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private static final float NUMERIC_COUNTER_SIZE = 20f;
    private static final float UI_MARGIN = 5f;
    private static final float HEART_SIZE = 10f;
    private static final int TARGET_FRAMERATE = 60;
    private static final float HALF = 0.5f;
    private static final int NUM_OF_SIDE_WALLS = 2;

    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String HEART_IMAGE_PATH = "assets/heart.png";

    private final int numOfBricks;
    private final int numOfRows;
    private GameObject ball;
    private GameObject paddle;
    private Brick[][] bricks;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private Counter livesCounter;
    private Counter bricksCounter;
    private UserInputListener inputListener;


    /**
     * Constructs the game manager.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param numOfBricks      The number of bricks in each row.
     * @param numOfRows        The number of rows of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numOfBricks, int numOfRows) {
        super(windowTitle, windowDimensions);
        this.numOfBricks = numOfBricks;
        this.numOfRows = numOfRows;
    }


    /**
     * Initializes the game: creates the background, ball, paddle, walls, bricks and
     * lives displays. Called at the start of every game, including after a reset.
     *
     * @param imageReader      Used to read images from disk.
     * @param soundReader      Used to read sounds from disk.
     * @param inputListener    Used to read the keys pressed by the player.
     * @param windowController Used to control the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.livesCounter = new Counter(PLAYER_LIVES);
        this.inputListener = inputListener;
        this.bricksCounter = new Counter(numOfBricks * numOfRows);

        windowController.setTargetFramerate(TARGET_FRAMERATE);
        createBackGround(windowDimensions, imageReader);
        createBall(imageReader, soundReader);
        createPaddle(windowDimensions, imageReader, inputListener);
        createWalls(windowDimensions);
        createBricks(windowDimensions, imageReader, soundReader);
        createNumericLifeCounter();
        createHearts(imageReader);
    }


    /**
     * Updates the game every frame, and checks whether the game has ended.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameStatus();
    }


    /**
     * Runs the game. The number of bricks in a row and the number of rows may be
     * given as command line arguments; otherwise, default values are used.
     *
     * @param args Optional: the number of bricks in a row, then the number of rows.
     */
    public static void main(String[] args) {
        int numOfBricks = DEFAULT_NUM_OF_BRICKS;
        int numOfRows = DEFAULT_NUM_OF_ROWS;
        if (args.length == NUM_OF_USER_INPUT) {
            numOfBricks = Integer.parseInt(args[0]);
            numOfRows = Integer.parseInt(args[1]);
        }
        new BrickerGameManager("Bricker", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT),
                numOfBricks, numOfRows).run();
    }


    /**
     * Checks whether the player won (no bricks left, or W was pressed) or lost a life
     * (the main ball fell below the window). Only the main ball affects the lives.
     */
    private void checkGameStatus() {
        if (bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            if (windowController.openYesNoDialog("You win! Play again?")) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        } else if (ball.getCenter().y() > windowDimensions.y()) {
            this.livesCounter.decrement();
            if (this.livesCounter.value() > 0) {
                resetBall();
            } else {
                if (windowController.openYesNoDialog("You lose! Play again?")) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            }
        }
    }


    /**
     * Places the main ball in the center of the window, moving diagonally down,
     * either to the left or to the right at random.
     */
    private void resetBall() {
        ball.setCenter(windowDimensions.mult(HALF));
        float ballVelX = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, BALL_SPEED));
    }


    /**
     * Creates the grid of bricks. Each brick gets a collision strategy drawn at
     * random by the strategy factory, and is stored in the bricks grid.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param imageReader      Used to read the brick image.
     * @param soundReader      Used by the strategy factory to read sounds.
     */
    private void createBricks(Vector2 windowDimensions, ImageReader imageReader, SoundReader soundReader) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);
        this.bricks = new Brick[numOfRows][numOfBricks];
        StrategyFactory strategyFactory = new StrategyFactory(
                gameObjects(), bricksCounter, livesCounter, imageReader, soundReader,
                inputListener, windowDimensions, paddle, bricks,
                new Vector2(BALL_SIZE, BALL_SIZE),
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                new Vector2(HEART_SIZE, HEART_SIZE),
                BALL_SPEED, MAX_LIVES);
        float totalSpacing = (numOfBricks + 1) * BRICK_SPACING;
        float brickWidth = (windowDimensions.x() - NUM_OF_SIDE_WALLS * WALL_WIDTH - totalSpacing) / numOfBricks;

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfBricks; col++) {
                float x = WALL_WIDTH + BRICK_SPACING + col * (brickWidth + BRICK_SPACING);
                float y = WALL_WIDTH + BRICK_SPACING + row * (BRICK_HEIGHT + BRICK_SPACING);
                Brick brick = new Brick(
                        new Vector2(x, y),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage,
                        row,
                        col,
                        strategyFactory.createRandomStrategy());
                bricks[row][col] = brick;
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }


    /**
     * Creates the player's paddle at the bottom center of the window.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param imageReader      Used to read the paddle image.
     * @param inputListener    Used by the paddle to read the arrow keys.
     */
    private void createPaddle(Vector2 windowDimensions, ImageReader imageReader,
                              UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() * HALF,
                windowDimensions.y() - PADDLE_BOTTOM_OFFSET));
        gameObjects().addGameObject(paddle);
    }


    /**
     * Creates invisible walls at the top, left and right of the window.
     * There is no bottom wall, so the ball can fall out of the window.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void createWalls(Vector2 windowDimensions) {
        GameObject topWall = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(topWall);
        GameObject leftWall = new GameObject(Vector2.ZERO,
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(leftWall);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(rightWall);
    }


    /**
     * Creates the background image of the game.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param imageReader      Used to read the background image.
     */
    private void createBackGround(Vector2 windowDimensions, ImageReader imageReader) {
        Renderable backGroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject backGround = new GameObject(Vector2.ZERO, windowDimensions, backGroundImage);
        gameObjects().addGameObject(backGround, Layer.BACKGROUND);
    }


    /**
     * Creates the main ball in the center of the window.
     *
     * @param imageReader Used to read the ball image.
     * @param soundReader Used to read the ball's collision sound.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        resetBall();
        gameObjects().addGameObject(this.ball);
    }


    /**
     * Creates the numeric lives display at the bottom left of the window.
     */
    private void createNumericLifeCounter() {
        Vector2 topLeftCorner = new Vector2(
                WALL_WIDTH + UI_MARGIN,
                windowDimensions.y() - NUMERIC_COUNTER_SIZE - UI_MARGIN);
        GameObject numericLifeCounter = new NumericLifeCounter(
                this.livesCounter,
                topLeftCorner,
                new Vector2(NUMERIC_COUNTER_SIZE, NUMERIC_COUNTER_SIZE));
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);
    }


    /**
     * Creates the graphic lives display (hearts), next to the numeric display.
     *
     * @param imageReader Used to read the heart image.
     */
    private void createHearts(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 topLeftCorner = new Vector2(
                WALL_WIDTH + UI_MARGIN + NUMERIC_COUNTER_SIZE + UI_MARGIN,
                windowDimensions.y() - HEART_SIZE - UI_MARGIN);
        GameObject graphicLifeCounter = new GraphicLifeCounter(
                topLeftCorner,
                new Vector2(HEART_SIZE, HEART_SIZE),
                livesCounter,
                heartImage,
                gameObjects(),
                PLAYER_LIVES,
                MAX_LIVES);
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }
}