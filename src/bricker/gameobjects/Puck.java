package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * A puck: an extra ball released from a broken brick. It behaves like a regular
 * ball, but losing it does not cost the player a life. Once it falls below the
 * window, it removes itself from the game.
 */
public class Puck extends Ball {

    private final float windowHeight;
    private final GameObjectCollection gameObjectCollection;


    /**
     * Constructs a puck.
     *
     * @param topLeftCorner        Position of the puck, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height of the puck, in window coordinates.
     * @param renderable           The renderable representing the puck.
     * @param collisionSound       The sound played when the puck collides with an object.
     * @param windowHeight         The height of the game window.
     * @param gameObjectCollection The game's object collection, to remove the puck from.
     */
    public Puck(Vector2 topLeftCorner,
                Vector2 dimensions,
                Renderable renderable,
                Sound collisionSound,
                float windowHeight,
                GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.windowHeight = windowHeight;
        this.gameObjectCollection = gameObjectCollection;
    }


    /**
     * Moves the puck, and removes it once it has fallen below the window.
     *
     * @param deltaTime The time elapsed since the last frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTopLeftCorner().y() > this.windowHeight) {
            gameObjectCollection.removeGameObject(this);
        }
    }
}