package com.game.core.entities.bonus;

import com.game.core.effects.*;

public class BonusFactory {
    public static Bonus createBonus(BonusType type) {
        return switch (type) {
            case SHIELD -> new Bonus(new ShieldEffect());
            case SPEED_BOOST -> new Bonus(new SpeedBoostEffect());
            case DOUBLE_SHOOT -> new Bonus(new DoubleShootEffect());
            case DOUBLE_DAMAGE -> new Bonus(new DoubleDamageEffect());
            case CIRCULAR_SHOOT -> new Bonus(new CircularShootEffect());
            default -> new Bonus(new NoEffect());
        };
    }
}
