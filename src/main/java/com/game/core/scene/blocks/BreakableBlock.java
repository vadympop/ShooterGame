package com.game.core.scene.blocks;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import com.game.core.managers.CollisionVisitor;

public class BreakableBlock extends Entity implements Block {
    private int durability;

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void update() {

    }

    public int getDurability() {
        return this.durability;
    }

    public void takeDamage() {

    }
}
