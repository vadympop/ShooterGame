package com.game.core.effects;

import com.game.core.entities.Player;

public class ShieldEffect implements Effect {
    private final float duration = 10f;

    @Override
    public void apply(Player player) {
        player.setHasShield(true);
    }

    @Override
    public void remove(Player player) {
        player.setHasShield(false);
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
