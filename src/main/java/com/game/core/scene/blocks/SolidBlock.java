package com.game.core.scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.DebugUtils;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

/**
 * Represents a solid, non-passable block in the game.
 * This block has a graphical tile and is involved in collision detection.
 */
public class SolidBlock extends CollidableGameObject implements Block {
    private final Tile tile;

    /**
     * Constructs a new SolidBlock with the specified tile and hitbox.
     *
     * @param tile   The graphical tile representing this block.
     * @param hitbox The collision hitbox defining the block's bounds.
     */
    public SolidBlock(Tile tile, Bounds hitbox) {
        super(hitbox);

        this.tile = Objects.requireNonNull(tile);
    }

    /**
     * Handles the collision behavior when this block collides with another object.
     *
     * @param visitor The collision visitor handling the collision logic.
     * @param other   The other collidable object involved in the collision.
     */
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    /**
     * Draws this block to the specified graphics context.
     * The block's tile is rendered at the block's position, and its hitbox
     * can be optionally drawn for debugging purposes.
     *
     * @param gc The GraphicsContext used to render the block.
     */
    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
        DebugUtils.drawHitboxIfDebug(gc, getHitbox());
    }

    @Override public Tile getTile() { return tile; }
}
