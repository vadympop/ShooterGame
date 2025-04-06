package com.game.core.effects;

import com.game.core.entities.Player;

public class SpeedBoostEffect implements Effect {
    private final float duration = 30f;

    @Override
    public void apply(Player player) {
        player.setSpeed(player.getSpeed() * 2);
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
