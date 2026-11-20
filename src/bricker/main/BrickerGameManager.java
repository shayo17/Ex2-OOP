package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
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


public class BrickerGameManager extends GameManager {

    private static final int BALL_SPEED = 200;
    private static final int WALL_WIDTH = 3;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final int DEFAULT_ROWS = 7;
    private static final int DEFAULT_BRICKS = 8;
    private static final int NUM_OF_USER_INPUT = 2;
    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_SPACING = 2;
    private static final int PLAYER_LIVES = 3;
    private static final int MAX_LIVES = 4;
    private static final float NUMERIC_COUNTER_SIZE = 20f;
    private static final float UI_MARGIN = 5f;
    private static final float HEART_SIZE = 10f;


    private final int numOfBricks;
    private final int numOfRows;
    private GameObject ball;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private Counter livesCounter;
    private Counter bricksCounter;
    private UserInputListener inputListener;


    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int numOfBricks, int numOfRows) {
        super(windowTitle, windowDimensions);
        this.numOfBricks = numOfBricks;
        this.numOfRows = numOfRows;
    }




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

        Vector2 windowDimensions = windowController.getWindowDimensions();

        // create background
        createBackGround(windowDimensions, imageReader);

        // create ball
        createBall(windowDimensions, imageReader, soundReader);

        // create paddle
        createPaddle(windowDimensions, imageReader, inputListener);

        // create walls
        createWalls(windowDimensions);

        // create bricks
        createBricks(windowDimensions, imageReader);

        // create numeric lives
        createNumericLifeCounter();

        // create heart lives
        createHearts(imageReader);



    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkGameStatus();
    }

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
                ball.setCenter(windowDimensions.mult(0.5f));
                float ballVelX = BALL_SPEED;
                float ballVelY = BALL_SPEED;
                Random rand = new Random();
                if (rand.nextBoolean()) {
                    ballVelX *= -1;
                }
                ball.setVelocity(new Vector2(ballVelX, ballVelY));
            } else {
                if (windowController.openYesNoDialog("You lose! Play again?")) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            }
        }
    }

    private void createBricks(Vector2 windowDimensions, ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        CollisionStrategy basicCollisionStrategy = new BasicCollisionStrategy(gameObjects(), bricksCounter);

        float totalSpacing = (numOfBricks + 1) * BRICK_SPACING;
        float brickWidth = (windowDimensions.x() - 2 * WALL_WIDTH - totalSpacing) / numOfBricks;

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfBricks; col++) {
                float x = WALL_WIDTH + BRICK_SPACING + col * (brickWidth + BRICK_SPACING);
                float y = WALL_WIDTH + BRICK_SPACING + row * (BRICK_HEIGHT + BRICK_SPACING);
                GameObject brick = new Brick(
                        new Vector2(x, y),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage,
                        row,
                        col,
                        basicCollisionStrategy
                );
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createPaddle(Vector2 windowDimensions, ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);
    }

    private void createWalls(Vector2 windowDimensions){


        GameObject topWall = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), WALL_WIDTH), null);
        gameObjects().addGameObject(topWall);

        GameObject leftWall = new GameObject(Vector2.ZERO,new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(leftWall);

        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() -WALL_WIDTH, 0), new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(rightWall);
    }

    private void createBackGround(Vector2 windowDimensions, ImageReader imageReader){
        Renderable backGroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject backGround = new GameObject(Vector2.ZERO, windowDimensions, backGroundImage);
        gameObjects().addGameObject(backGround, Layer.BACKGROUND);

    }

    private void createBall(Vector2 windowDimensions, ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
        this.ball.setCenter(windowDimensions.mult(0.5f));

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }

        this.ball.setVelocity(new Vector2(ballVelX, ballVelY));
        gameObjects().addGameObject(this.ball);
    }

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

    private void createHearts(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Vector2 topLeftCorner = new Vector2(
                WALL_WIDTH + UI_MARGIN + NUMERIC_COUNTER_SIZE + UI_MARGIN,
                windowDimensions.y() - HEART_SIZE - UI_MARGIN
        );
        GameObject graphicLifeCounter = new GraphicLifeCounter(
                topLeftCorner,
                new Vector2(HEART_SIZE, HEART_SIZE),
                livesCounter,
                heartImage,
                gameObjects(),
                PLAYER_LIVES,
                MAX_LIVES
        );
        gameObjects().addGameObject(graphicLifeCounter, Layer.UI);
    }

    public static void main(String[] args) {

        int numOfBricks = DEFAULT_BRICKS;
        int numOfRows = DEFAULT_ROWS;

        if (args.length == NUM_OF_USER_INPUT){
            numOfBricks = Integer.parseInt(args[0]);
            numOfRows = Integer.parseInt(args[1]);
        }


        new BrickerGameManager("Bouncing ball", new Vector2(WINDOW_WIDTH,WINDOW_HEIGHT), numOfBricks, numOfRows).run();
    }
}
