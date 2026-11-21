package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.util.Counter;


/**
 * A collision strategy that breaks the brick and its four direct neighbors
 * (no diagonals). Each neighbor is broken with its own strategy, so a chain
 * reaction may occur.
 */
public class ExplodingStrategy extends BasicCollisionStrategy {
    private final Brick[][] bricks;
    private final Sound explosionSound;


    /**
     * Constructs an exploding strategy.
     *
     * @param gameObjects    The game's object collection.
     * @param bricksCounter  Counter of the bricks remaining in the game.
     * @param bricks         The grid of bricks, indexed by [row][col].
     * @param explosionSound The sound of the explosion.
     */
    public ExplodingStrategy(GameObjectCollection gameObjects,
                             Counter bricksCounter,
                             Brick[][] bricks,
                             Sound explosionSound) {
        super(gameObjects, bricksCounter);
        this.bricks = bricks;
        this.explosionSound = explosionSound;
    }


    /**
     * Breaks the brick at [r][c], if this position is inside the grid. The neighbor
     * is "hit" through its own collision handling, so its own strategy is activated.
     *
     * @param brick The exploding brick, passed to the neighbor as the hitting object.
     * @param r     The row of the neighbor.
     * @param c     The column of the neighbor.
     */
    private void breakNeighbor(Brick brick, int r, int c) {
        if (r >= 0 && r < bricks.length && c >= 0 && c < bricks[0].length) {
            bricks[r][c].onCollisionEnter(brick, null);
        }
    }


    /**
     * Plays the explosion sound and breaks the four direct neighbors of the brick.
     *
     * @param brick The brick that exploded.
     * @param other The object that broke the brick.
     */
    @Override
    protected void onBrickBroken(Brick brick, GameObject other) {
        explosionSound.play();
        int row = brick.getRow();
        int col = brick.getCol();
        breakNeighbor(brick, row - 1, col);
        breakNeighbor(brick, row + 1, col);
        breakNeighbor(brick, row, col - 1);
        breakNeighbor(brick, row, col + 1);
    }
}