package com.game.core.scene.areas;

import com.game.core.behaviour.base.GameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.Effect;
import com.game.core.effects.ReduceSpeedEffect;
import com.game.core.entities.Player;

public class SlowingArea extends GameObject implements Area  {
    private final Effect effect = new ReduceSpeedEffect();
    private Bounds bounds;

    public SlowingArea(Bounds bounds) {
        setBounds(bounds);
    }

    @Override
    public void applyEffect(Player player) {
        player.applyEffect(effect);
    }

    @Override
    public boolean contains(Player player) {
        return false;
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    private void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}
