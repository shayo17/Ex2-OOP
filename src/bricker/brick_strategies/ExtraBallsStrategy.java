package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;


/**
 * A collision strategy that breaks the brick and releases two pucks from its center.
 */
public class ExtraBallsStrategy extends BasicCollisionStrategy {
    private static final int NUM_OF_PUCK_BALLS = 2;
    private final GameObjectCollection gameObjects;
    private final Renderable puckImage;
    private final Sound collisionSound;
    private final Vector2 puckSize;
    private final float puckSpeed;
    private final float windowHeight;


    /**
     * Constructs an extra balls strategy.
     *
     * @param gameObjects    The game's object collection.
     * @param bricksCounter  Counter of the bricks remaining in the game.
     * @param puckImage      The image of a puck.
     * @param collisionSound The sound a puck makes on collision.
     * @param puckSize       The size of a puck.
     * @param puckSpeed      The speed of a puck.
     * @param windowHeight   The height of the game window.
     */
    public ExtraBallsStrategy(GameObjectCollection gameObjects,
                              Counter bricksCounter,
                              Renderable puckImage,
                              Sound collisionSound,
                              Vector2 puckSize,
                              float puckSpeed,
                              float windowHeight) {
        super(gameObjects, bricksCounter);
        this.gameObjects = gameObjects;
        this.puckImage = puckImage;
        this.collisionSound = collisionSound;
        this.puckSize = puckSize;
        this.puckSpeed = puckSpeed;
        this.windowHeight = windowHeight;
    }


    /**
     * Releases two pucks from the center of the broken brick, each moving in a
     * random direction.
     *
     * @param brick The brick that was broken.
     * @param other The object that broke the brick.
     */
    @Override
    protected void onBrickBroken(Brick brick, GameObject other) {
        Random random = new Random();
        for (int i = 0; i < NUM_OF_PUCK_BALLS; i++) {
            Puck puck = new Puck(Vector2.ZERO, puckSize, puckImage, collisionSound, windowHeight, gameObjects);
            puck.setCenter(brick.getCenter());
            double angle = random.nextDouble() * Math.PI;
            float velocityX = (float) Math.cos(angle) * puckSpeed;
            float velocityY = (float) Math.sin(angle) * puckSpeed;
            puck.setVelocity(new Vector2(velocityX, velocityY));
            gameObjects.addGameObject(puck);
        }
    }
}
