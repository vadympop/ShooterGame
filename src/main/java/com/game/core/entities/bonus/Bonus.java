package com.game.core.entities.bonus;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;

public class Bonus extends Entity {
    private final Effect effect;

    public Bonus(Effect effect, Tile tile, Bounds hitbox) {
        super(tile, hitbox);
        this.effect = effect;
    }

    public Effect getEffect() { return this.effect; }

    public void applyEffect(Player player) {
        boolean isApplied = player.applyEffect(this.getEffect());
        if (isApplied) this.setState(false);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) { visitor.visit(this, other); }
}
