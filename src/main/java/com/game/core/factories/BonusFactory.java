package com.game.core.factories;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.effects.*;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;

public class BonusFactory {
    public static Bonus create(BonusType type) {
        SceneConfig config = ConfigLoader.getInstance().getConfig();
        Tile tile = new Tile(config.getBonus().getTexture(), null);
        RectangleBounds hitbox = BoundsFactory.createForBlock(config.getBonus().getHitbox());

        return switch (type) {
            case SHIELD -> new Bonus(new ShieldEffect(), tile, hitbox);
            case SPEED_BOOST -> new Bonus(new SpeedBoostEffect(), tile, hitbox);
            case DOUBLE_SHOOT -> new Bonus(new DoubleShootEffect(), tile, hitbox);
            case DOUBLE_DAMAGE -> new Bonus(new DoubleDamageEffect(), tile, hitbox);
            case CIRCULAR_SHOOT -> new Bonus(new CircularShootEffect(), tile, hitbox);
            case null, default -> new Bonus(new NoEffect(), tile, hitbox);
        };
    }
}
