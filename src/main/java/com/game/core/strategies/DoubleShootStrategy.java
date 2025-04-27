package com.game.core.strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.utils.config.SceneConfig;

import java.util.List;
import java.util.Objects;

/**
 * A shooting strategy that fires two bullets at once when the {@code shoot} method is invoked.
 * This strategy uses a predefined bullet configuration to create each bullet.
 */
public class DoubleShootStrategy implements ShootingStrategy {
    private SceneConfig.BulletConfig bulletConfig;

    /**
     * Fires two bullets of the specified {@link BulletType}, using the given {@link Player} as the owner.
     *
     * @param player     The player instance that owns the bullets.
     * @param bulletType The type of the bullets to be fired.
     * @return A {@link List} containing two {@link Bullet} instances representing the fired bullets.
     */
    @Override
    public List<Bullet> shoot(Player player, BulletType bulletType) {
        return List.of(
            new Bullet.builder(bulletType).config(getBulletConfig()).owner(player).build(),
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
