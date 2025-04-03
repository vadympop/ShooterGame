package com.game.core.entities.bonus;

import com.game.core.effects.*;
import com.game.core.scene.graphics.Tile;

public class BonusFactory {
    public static Bonus createBonus(BonusType type) {
        Tile tile = new Tile("bonus.png", 3);
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
