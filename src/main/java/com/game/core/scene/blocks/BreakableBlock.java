package com.game.core.scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreakableBlock extends Entity implements Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(BreakableBlock.class);

    private int durability = 10;

    public BreakableBlock(Tile tile, Bounds hitbox) {
        super(tile, hitbox);
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) { visitor.visit(this, other); }

    @Override
    public void update(double deltaTime) {

    }

    public float getDurability() { return this.durability; }

    public void takeDamage(int damage) {
        durability -= damage;
        if (getDurability() <= 0) {
            LOGGER.info("Breakable block {} has been broken", this);
            setState(false);
        }
    }
}
