package com.game.core.effects;

import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;

public class DoubleDamageEffect implements Effect {
    private final float duration = 10f;

    @Override
    public void apply(Player player) {
        player.getSm().setBulletType(BulletType.DOUBLE_DAMAGED_STANDARD);
    }

    @Override
    public void remove(Player player) {
        player.getSm().setBulletType(BulletType.STANDARD);
    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
