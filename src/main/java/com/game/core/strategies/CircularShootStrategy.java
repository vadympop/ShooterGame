package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.utils.config.SceneConfig;

import java.util.ArrayList;
import java.util.List;

public class CircularShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    @Override
    public List<Bullet> shoot(Player player) {
        List<Bullet> bullets = new ArrayList<>();

        float startAngle = player.getRotationAngle();
        for (float i = startAngle; i < 360 + startAngle; i += 45) {
            bullets.add(
                new Bullet.builder(player.getBulletType())
                        .config(getBulletConfig())
                        .owner(player)
                        .rotationAngle(i % 360)
                        .build()
                );
        }

        return bullets;
    }

    @Override
    public void setBulletConfig(SceneConfig.BulletConfig config) { this.bulletConfig = config; }
    @Override
    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
}
