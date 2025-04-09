package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.entities.Player;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;

public class PlayerFactory {
    public static Player create(PlayerSpawner spawner) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile bulletTile = new Tile(config.getBullet().getTexture(), null);
        Bounds hitbox = BoundsFactory.createFromConfig(config.getPlayer().getHitbox());

        return new Player(
                spawner,
                hitbox,
                spawner.getPlayerTile(),
                bulletTile,
                config.getPlayer().getMaxHealth(),
                config.getPlayer().getMaxBulletsCount(),
                config.getPlayer().getBulletDamage(),
                config.getPlayer().getBulletsReloadDelay(),
                config.getPlayer().getSpeed(),
                config.getPlayer().getRotationSpeed()
        );
    }
}
