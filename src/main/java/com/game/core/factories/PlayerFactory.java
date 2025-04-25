package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.entities.Player;
import com.game.core.enums.GameModeEnum;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;

import java.util.Objects;

/**
 * A factory class responsible for creating instances of {@link Player}.
 * It uses configuration settings from {@link ConfigManager} to initialize
 * the {@link Player} objects with appropriate parameters.
 */
public class PlayerFactory {
    /**
     * Creates a new {@link Player} instance based on the provided {@link PlayerSpawner}.
     *
     * @param spawner the spawner responsible for determining the initial position of the player
     * @return a fully initialized {@link Player} object
     * @throws NullPointerException if the spawner is null
     */
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
