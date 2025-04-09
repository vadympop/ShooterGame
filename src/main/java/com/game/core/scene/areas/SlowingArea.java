package com.game.core.scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.ReduceSpeedEffect;

public class SlowingArea extends Area {
    public SlowingArea(Bounds bounds) {
        super(new ReduceSpeedEffect());
        setBounds(bounds);
    }
}
