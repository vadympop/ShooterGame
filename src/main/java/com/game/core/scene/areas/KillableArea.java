package com.game.core.scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.TakeDamageEffect;

/**
 * Represents an area in the game where entities can take damage.
 * This area uses {@link TakeDamageEffect} to apply damage to entities that enter its bounds.
 */
public class KillableArea extends Area {
    public KillableArea(Bounds bounds) {
        super(new TakeDamageEffect(), bounds);
    }
}
