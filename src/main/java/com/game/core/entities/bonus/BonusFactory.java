package com.game.core.entities.bonus;

import com.game.core.effects.*;

public class BonusFactory {
    public static Bonus createBonus(BonusType type) {
        Bonus createdBonus;
        switch (type) {
            case SHIELD -> createdBonus = new Bonus(new ShieldEffect());
            case SPEED_BOOST -> createdBonus = new Bonus(new SpeedBoostEffect());
            case DOUBLE_SHOOT -> createdBonus = new Bonus(new DoubleShootEffect());
            case DOUBLE_DAMAGE -> createdBonus = new Bonus(new DoubleDamageEffect());
            case CIRCULAR_SHOOT -> createdBonus = new Bonus(new CircularShootEffect());
            default -> createdBonus = new Bonus(new NoEffect());
        }

        return createdBonus;
    }
}
