package com.game.core.effects;

import com.game.core.entities.Player;

public class TakeDamageEffect implements Effect {
    private final float duration = 20f;
    private final int damage = 1;

    @Override
    public void apply(Player player) {
        if (player.getHealth() - damage <= 0) return;

        player.takeDamage(damage);
    }

    @Override
    public void remove(Player player) {}

    @Override
    public float getDuration() {
        return duration;
    }
}
