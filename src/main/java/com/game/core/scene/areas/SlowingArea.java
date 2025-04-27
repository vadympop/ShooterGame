package com.game.core.scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.ReduceSpeedEffect;

/**
 * Represents an area in the game where entities experience a reduction in speed.
 * The effect of slowing down entities is achieved by applying the {@link ReduceSpeedEffect}
 * within the specified boundary.
 */
public class SlowingArea extends Area {
    public SlowingArea(Bounds bounds) {
        super(new ReduceSpeedEffect(), bounds);
    }
}
