package com.game.core.scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a block in the game that has durability and can break upon taking sufficient damage.
 */
public class BreakableBlock extends Entity implements Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(BreakableBlock.class);

    private int durability = 10;

    /**
     * Creates a new breakable block.
     *
     * @param tile   the graphical tile representing this block
     * @param hitbox the bounds of the block used for collision detection
     */
    public BreakableBlock(Tile tile, Bounds hitbox) {
        super(tile, hitbox);
    }

    /**
     * Handles collision logic when this block collides with another object.
     *
     * @param visitor the visitor handling the collision logic
     * @param other   the other collidable object
     */
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void update(double deltaTime) {

    }

    public float getDurability() { return this.durability; }

    /**
     * Reduces the block's durability by the specified damage amount. Breaks the block if
     * the durability drops to zero or below.
     *
     * @param damage the amount of damage to apply to the block
     */
    public void takeDamage(int damage) {
        durability -= damage;
        if (getDurability() <= 0) {
            LOGGER.info("Breakable block {} has been broken", this);
            setState(false);
        }
    }
}
