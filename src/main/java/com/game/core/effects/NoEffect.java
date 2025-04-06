package com.game.core.effects;

import com.game.core.entities.Player;

public class NoEffect implements Effect {
    private final float duration = 600f;

    @Override
    public void apply(Player player) {
        if (player.getActiveEffect() == null) return;

        player.getActiveEffect().remove(player);
    }

    @Override
    public void remove(Player player) {}

    @Override
    public float getDuration() {
        return this.duration;
    }
}
