package com.game.core.scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.TakeDamageEffect;

public class KillableArea extends Area {
    public KillableArea(Bounds bounds) {
        super(new TakeDamageEffect());
        setBounds(bounds);
    }
}
