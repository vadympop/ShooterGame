package com.game.core.utils;

import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.effects.*;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;

public class GameObjectsFactory {
    public static Player createPlayer(PlayerSpawner spawner) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile bulletTile = new Tile(config.getBullet().getTexture(), null);
        CircleBounds hitbox = new CircleBounds(config.getPlayer().getHitbox().getRadius());

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

    public static Block createBlock(SceneConfig.MappingBlockConfig blockConfig, Tile tile) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        SceneConfig.BoundsConfig hitboxConfig = blockConfig.getHitbox();
        float width = hitboxConfig != null ? hitboxConfig.getWidth() : config.getTileWidth();
        float height = hitboxConfig != null ? hitboxConfig.getHeight() : config.getTileHeight();
        RectangleBounds hitbox = new RectangleBounds(width, height);

        Block block;
        if (blockConfig.isBreakable()) block = new BreakableBlock(tile, hitbox);
        else block = new SolidBlock(tile, hitbox);

        return block;
    }

    public static Bonus createBonus(BonusType type) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile tile = new Tile(config.getBonusTexture(), null);
        RectangleBounds hitbox = new RectangleBounds(32, 32); // CHANGE LATER

        return switch (type) {
            case SHIELD -> new Bonus(new ShieldEffect(), tile, hitbox);
            case SPEED_BOOST -> new Bonus(new SpeedBoostEffect(), tile, hitbox);
            case DOUBLE_SHOOT -> new Bonus(new DoubleShootEffect(), tile, hitbox);
            case DOUBLE_DAMAGE -> new Bonus(new DoubleDamageEffect(), tile, hitbox);
            case CIRCULAR_SHOOT -> new Bonus(new CircularShootEffect(), tile, hitbox);
            default -> new Bonus(new NoEffect(), tile, hitbox);
        };
    }

}
