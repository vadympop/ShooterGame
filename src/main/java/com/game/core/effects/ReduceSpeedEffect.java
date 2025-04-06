package com.game.core.effects;

import com.game.core.entities.Player;

public class ReduceSpeedEffect implements Effect {
    private final float duration = 5f;

    @Override
    public void apply(Player player) {
        player.setSpeed(player.getSpeed() * 0.75f);
    }

    @Override
    public void remove(Player player) {
        player.setSpeed(player.getDefaultSpeed());
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
