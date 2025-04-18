package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CircularShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    @Override
    public List<Bullet> shoot(Player player, BulletType bulletType) {
        List<Bullet> bullets = new ArrayList<>();

        float startAngle = player.getRotationAngle();
        for (float i = startAngle; i < 360 + startAngle; i += 45) {
            bullets.add(
                new Bullet.builder(bulletType)
                        .config(getBulletConfig())
                        .owner(player)
                        .rotationAngle(i % 360)
                        .build()
                );
        }

        return bullets;
    }

    @Override
    public void setBulletConfig(SceneConfig.BulletConfig config) {
        this.bulletConfig = Objects.requireNonNull(config);
    }
    @Override
    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
}
