import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import gameobjects.Ball;

public class BouncingBallGameManager extends GameManager {

    public BouncingBallGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }


    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);


        // create ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(20,20), ballImage);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        ball.setVelocity(Vector2.DOWN.mult(200));
        gameObjects().addGameObject(ball);

        //create paddles
        int[] paddleHeights = {(int)windowDimensions.y() - 30, 30};
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);

        for (int i = 0; i < paddleHeights.length; i++) {
            GameObject paddle = new GameObject(Vector2.ZERO, new Vector2(100, 15), paddleImage);
            paddle.setCenter(new Vector2(windowDimensions.x()/2, paddleHeights[i]));
            gameObjects().addGameObject(paddle);

        }


    }

    public static void main(String[] args) {

        new BouncingBallGameManager("Bouncing ball", new Vector2(700,500)).run();
    }
}
