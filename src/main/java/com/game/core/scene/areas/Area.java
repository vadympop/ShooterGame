package com.game.core.scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.entities.Player;

public interface Area {
    void applyEffect(Player player);
    boolean contains(Player player);
    Bounds getBounds();
}
