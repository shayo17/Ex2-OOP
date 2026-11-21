package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * A brick in the bricks grid. When hit, the brick delegates the handling of the
 * collision to its collision strategy, which decides what happens.
 */
public class Brick extends GameObject {

    private final int row;
    private final int col;
    private final CollisionStrategy collisionStrategy;


    /**
     * Constructs a brick.
     *
     * @param topLeftCorner     Position of the brick, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height of the brick, in window coordinates.
     * @param renderable        The renderable representing the brick.
     * @param row               The row of the brick in the bricks grid.
     * @param col               The column of the brick in the bricks grid.
     * @param collisionStrategy The strategy that is activated when the brick is hit.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, int row, int col, CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.row = row;
        this.col = col;
        this.collisionStrategy = collisionStrategy;
    }


    /**
     * Returns the row of the brick in the bricks grid.
     *
     * @return The row of the brick.
     */
    public int getRow(){
        return this.row;
    }


    /**
     * Returns the column of the brick in the bricks grid.
     *
     * @return The column of the brick.
     */
    public int getCol(){
        return this.col;
    }


    /**
     * Activates the brick's collision strategy.
     *
     * @param other     The object that hit the brick.
     * @param collision Information about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionStrategy.onCollision(this, other);
    }
}