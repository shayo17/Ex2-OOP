package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.Color;

public class NumericLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final TextRenderable textRenderable;

    /**
     - @param livesCounter  Counter tracking the player's remaining lives.
     - @param topLeftCorner Position of the object, in window coordinates (pixels).
     -                      Note that (0,0) is the top-left corner of the window.
     - @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        this.textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(this.textRenderable);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int lives = livesCounter.value();
        textRenderable.setString(Integer.toString(lives));
        if (lives >= 3) {
            textRenderable.setColor(Color.GREEN);
        } else if (lives == 2) {
            textRenderable.setColor(Color.YELLOW);
        } else if (lives == 1) {
            textRenderable.setColor(Color.RED);
        }
    }
}
