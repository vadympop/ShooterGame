package com.game.core.scene.blocks;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import com.game.core.controllers.CollisionVisitor;
import com.game.core.scene.graphics.Tile;

public class BreakableBlock extends Entity implements Block {
    private int durability = 10;

    public BreakableBlock(Tile tile) {
        super(tile);
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
            setState(false);
        }
    }
}
