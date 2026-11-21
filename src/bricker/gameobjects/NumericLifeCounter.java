package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;


/**
 * A numeric display of the player's lives. The number and its color follow the
 * shared lives counter every frame: green for 3 lives or more, yellow for 2,
 * and red for 1.
 */
public class NumericLifeCounter extends GameObject {

    private static final int GREEN_MIN_LIVES = 3;
    private static final int YELLOW_LIVES = 2;
    private static final int RED_LIVES = 1;

    private final Counter livesCounter;
    private final TextRenderable textRenderable;


    /**
     * Constructs a numeric life counter.
     *
     * @param livesCounter  Counter tracking the player's remaining lives.
     * @param topLeftCorner Position of the counter, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height of the counter, in window coordinates.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        this.textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(this.textRenderable);
    }


    /**
     * Updates the displayed number and its color to match the current number of lives.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int lives = livesCounter.value();
        textRenderable.setString(Integer.toString(lives));
        if (lives >= GREEN_MIN_LIVES) {
            textRenderable.setColor(Color.GREEN);
        } else if (lives == YELLOW_LIVES) {
            textRenderable.setColor(Color.YELLOW);
        } else if (lives == RED_LIVES) {
            textRenderable.setColor(Color.RED);
        }
    }
}
