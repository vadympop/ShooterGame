package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.entities.Player;
import com.game.core.enums.GameModeEnum;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;

import java.util.Objects;

public class PlayerFactory {
    public static Player create(PlayerSpawner spawner) {
        Objects.requireNonNull(spawner);

        ConfigManager cfgLoader = ConfigManager.getInstance();
        SceneConfig config = cfgLoader.getConfig();
        Bounds hitbox = BoundsFactory.createFromConfig(config.getPlayer().getHitbox());

        return new Player(
                spawner,
                hitbox,
                spawner.getPlayerTile(),
                config.getBullet(),
                config.getPlayer().getMaxHealth(),
                config.getPlayer().getMaxBulletsCount(),
                config.getPlayer().getShieldHitboxMultiplier(),
                config.getPlayer().getBulletsReloadDelay(),
                config.getPlayer().getBulletsCooldown(),
                config.getPlayer().getSpeed(),
                config.getPlayer().getRotationSpeed(),
                cfgLoader.getGameMode() == GameModeEnum.INFINITY_BULLETS
        );
    }
}
