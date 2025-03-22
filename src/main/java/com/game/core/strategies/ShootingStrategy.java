package com.game.core.strategies;

import com.game.core.entities.Bullet;
import com.game.core.entities.Player;

import java.util.List;

public interface ShootingStrategy {
    List<Bullet> shoot(Player player);
}
