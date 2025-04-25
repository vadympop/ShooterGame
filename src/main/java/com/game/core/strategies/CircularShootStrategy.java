package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A shooting strategy that deploys bullets in a circular pattern around the player.
 */
public class CircularShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    /**
     * Shoots bullets in a circular pattern around the player.
     *
     * @param player     The player shooting the bullets.
     * @param bulletType The type of the bullets to be shot.
     * @return A list of bullets arranged in a circular pattern.
     */
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
