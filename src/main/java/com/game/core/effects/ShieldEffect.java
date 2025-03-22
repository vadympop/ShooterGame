package com.game.core.effects;

import com.game.core.entities.Player;

public class ShieldEffect implements Effect {
    private final float duration = 20f;

    @Override
    public void apply(Player player) {

    }

    @Override
    public void remove(Player player) {

    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
