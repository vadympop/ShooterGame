package com.game.core.utils;

import com.game.core.effects.*;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;

public class GameObjectsFactory {
    public static Player createPlayer(PlayerSpawner spawner) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile bulletTile = new Tile(config.getBullet().getTexture(), null);
        return new Player(
                spawner,
                spawner.getPlayerTile(),
                bulletTile,
                config.getPlayer().getBoundsRadius(),
                config.getPlayer().getMaxHealth(),
                config.getPlayer().getMaxBulletsCount(),
                config.getPlayer().getBulletDamage(),
                config.getPlayer().getBulletsReloadDelay(),
                config.getPlayer().getSpeed(),
                config.getPlayer().getRotationSpeed()
        );
    }

    public static Bonus createBonus(BonusType type) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile tile = new Tile(config.getBonusTexture(), null);
        return switch (type) {
            case SHIELD -> new Bonus(new ShieldEffect(), tile);
            case SPEED_BOOST -> new Bonus(new SpeedBoostEffect(), tile);
            case DOUBLE_SHOOT -> new Bonus(new DoubleShootEffect(), tile);
            case DOUBLE_DAMAGE -> new Bonus(new DoubleDamageEffect(), tile);
            case CIRCULAR_SHOOT -> new Bonus(new CircularShootEffect(), tile);
            default -> new Bonus(new NoEffect(), tile);
        };
    }

}
