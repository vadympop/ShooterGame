package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.List;

public interface ShootingStrategy {
    List<Bullet> shoot(Player player, BulletType bulletType);
    void setBulletConfig(SceneConfig.BulletConfig config);
    SceneConfig.BulletConfig getBulletConfig();
}
