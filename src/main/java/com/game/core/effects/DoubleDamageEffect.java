package com.game.core.effects;

import com.game.core.entities.Player;

public class DoubleDamageEffect implements Effect {
    private final float duration = 10f;

    @Override
    public void apply(Player player) {
        player.setBulletDamage(player.getBulletDamage() * 2);
    }

    @Override
    public void remove(Player player) {
        player.setBulletDamage(player.getBulletDamage() / 2);
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
