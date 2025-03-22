package com.game.core.effects;

import com.game.core.entities.Player;

public interface Effect {
    void apply(Player player);
    void remove(Player player);
    float getDuration();
}
