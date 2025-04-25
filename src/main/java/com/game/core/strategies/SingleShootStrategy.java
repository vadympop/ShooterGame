package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.List;
import java.util.Objects;

/**
 * A shooting strategy that allows a player to shoot a single bullet.
 * This strategy configures bullets based on the provided {@link SceneConfig.BulletConfig}.
 * Each bullet is assigned a player as its owner and a specific {@link BulletType}.
 */
public class SingleShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    /**
     * Shoots a single bullet based on the specified player and bullet type.
     *
     * @param player     The player who is initiating the shot; serves as the bullet's owner.
     * @param bulletType The type of the bullet to be created (e.g., speed, damage, etc.).
     * @return A list containing a single bullet object configured with the provided parameters.
     */
    @Override
    public List<Bullet> shoot(Player player, BulletType bulletType) {
        return List.of(
                new Bullet.builder(bulletType).config(getBulletConfig()).owner(player).build()
        );
    }

    @Override
    public void setBulletConfig(SceneConfig.BulletConfig config) {
        this.bulletConfig = Objects.requireNonNull(config);
    }
    @Override
    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
}
