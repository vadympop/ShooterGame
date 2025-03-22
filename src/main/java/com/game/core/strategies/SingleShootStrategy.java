package com.game.core.strategies;

import com.game.core.entities.Bullet;
import com.game.core.entities.Player;

import java.util.Collections;
import java.util.List;

public class SingleShootStrategy implements ShootingStrategy {
    @Override
    public List<Bullet> shoot(Player player) {
        return Collections.singletonList(
                new Bullet.builder()
                        .owner(player)
                        .pos(player.getX(), player.getY())
                        .rotationAngle(player.getRotationAngle())
                        .build()
        );
    }
}
