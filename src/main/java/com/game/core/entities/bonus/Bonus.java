package com.game.core.entities.bonus;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.managers.CollisionVisitor;

public class Bonus extends Entity {
    private final Effect effect;

    public Bonus(Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void applyEffect(Player player) {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void update() {

    }
}
