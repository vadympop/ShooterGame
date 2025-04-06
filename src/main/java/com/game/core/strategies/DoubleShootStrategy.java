package com.game.core.strategies;

import com.game.core.entities.Bullet;
import com.game.core.entities.Player;

import java.util.List;

public class DoubleShootStrategy implements ShootingStrategy {
    @Override
    public List<Bullet> shoot(Player player) {
        return List.of(
            new Bullet.builder().owner(player).build(),
            new Bullet.builder().owner(player).build()
        );
    }
}
