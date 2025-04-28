package com.game.core.effects;

import com.game.core.entities.Player;
import com.game.core.strategies.CircularShootStrategy;
import com.game.core.strategies.SingleShootStrategy;

public class CircularShootEffect implements Effect {
    private final float duration = 10f;

    @Override
    public void apply(Player player) {
        player.getSm().setShootingStrategy(new CircularShootStrategy());
    }

    @Override
    public void remove(Player player) {
        player.getSm().setShootingStrategy(new SingleShootStrategy());
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
