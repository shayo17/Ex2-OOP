package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private static final float HEART_SPACING = 5f;

    private final Counter livesCounter;
    private final GameObjectCollection gameObjects;
    private final Renderable heartRenderable;
    private final Vector2 widgetTopLeftCorner;
    private final Vector2 heartDimensions;
    private final GameObject[] hearts;
    private int currentDisplayedHearts;

    /**
     - @param widgetTopLeftCorner Position of the widget, in window coordinates (pixels).
     -                            Note that (0,0) is the top-left corner of the window.
     - @param heartDimensions     Width and height of a single heart in window coordinates.
     - @param livesCounter        Counter tracking the player's remaining lives.
     - @param heartRenderable     The renderable representing a heart image.
     - @param gameObjects         Collection of game objects to add/remove hearts from.
     - @param numOfLives          Initial number of lives to display.
     - @param maxLives            Maximum number of lives allowed.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 heartDimensions,
                              Counter livesCounter,
                              Renderable heartRenderable,
                              GameObjectCollection gameObjects,
                              int numOfLives,
                              int maxLives) {
        super(widgetTopLeftCorner, Vector2.ZERO, null);
        this.widgetTopLeftCorner = widgetTopLeftCorner;
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.heartRenderable = heartRenderable;
        this.gameObjects = gameObjects;
        this.hearts = new GameObject[maxLives];
        this.currentDisplayedHearts = 0;

        for (int i = 0; i < numOfLives; i++) {
            addHeart();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        while (currentDisplayedHearts > livesCounter.value() && currentDisplayedHearts > 0) {
            currentDisplayedHearts--;
            gameObjects.removeGameObject(hearts[currentDisplayedHearts], Layer.UI);
            hearts[currentDisplayedHearts] = null;
        }
        while (currentDisplayedHearts < livesCounter.value() && currentDisplayedHearts < hearts.length) {
            addHeart();
        }
    }

    private void addHeart() {
        float x = widgetTopLeftCorner.x() + currentDisplayedHearts * (heartDimensions.x() + HEART_SPACING);
        Vector2 position = new Vector2(x, widgetTopLeftCorner.y());
        GameObject heart = new GameObject(position, heartDimensions, heartRenderable);
        hearts[currentDisplayedHearts] = heart;
        gameObjects.addGameObject(heart, Layer.UI);
        currentDisplayedHearts++;
    }
}