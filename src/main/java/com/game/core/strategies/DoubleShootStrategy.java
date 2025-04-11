package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.List;

public class DoubleShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    @Override
    public List<Bullet> shoot(Player player, BulletType bulletType) {
        return List.of(
            new Bullet.builder(bulletType).config(getBulletConfig()).owner(player).build(),
            new Bullet.builder(bulletType).config(getBulletConfig()).owner(player).build()
        );
    }

    @Override
    public void setBulletConfig(SceneConfig.BulletConfig config) { this.bulletConfig = config; }
    @Override
    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
}
