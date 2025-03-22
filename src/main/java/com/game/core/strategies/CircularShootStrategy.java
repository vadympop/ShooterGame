package com.game.core.strategies;

import com.game.core.entities.Bullet;
import com.game.core.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class CircularShootStrategy implements ShootingStrategy {
    @Override
    public List<Bullet> shoot(Player player) {
        List<Bullet> bullets = new ArrayList<>();

        float startAngle = player.getRotationAngle();
        for (float i = startAngle; i < 360 + startAngle; i += 45) {
            bullets.add(
                    new Bullet.builder()
                            .owner(player)
                            .pos(player.getX(), player.getY())
                            .rotationAngle(i)
                            .build()
                    );
        }

        return bullets;
    }
}
