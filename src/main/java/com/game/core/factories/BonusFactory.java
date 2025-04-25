package com.game.core.factories;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.effects.*;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;

/**
 * A factory class responsible for creating instances of {@link Bonus} based on a given {@link BonusType}.
 * It utilizes configuration settings to determine the properties of the created bonuses,
 * such as their texture, hitbox, and associated effects.
 */
public class BonusFactory {
    /**
     * Creates a new {@link Bonus} instance based on the specified {@link BonusType}.
     * The bonus will have a predefined texture, hitbox, and behavior effect configured via the scene config.
     *
     * @param type the type of bonus to create; determines the specific effect applied
     * @return a new {@link Bonus} instance with the appropriate configuration and effect
     */
    public static Bonus create(BonusType type) {
        SceneConfig config = ConfigManager.getInstance().getConfig();
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
