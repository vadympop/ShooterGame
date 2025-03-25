package com.game.core.effects;

import com.game.core.entities.Player;
import com.game.core.strategies.DoubleShootStrategy;
import com.game.core.strategies.SingleShootStrategy;

public class DoubleShootEffect implements Effect {
    private final float duration = 20f;

    @Override
    public void apply(Player player) {
        player.setShootingStrategy(new DoubleShootStrategy());
    }

    @Override
    public void remove(Player player) {
        player.setShootingStrategy(new SingleShootStrategy());
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
