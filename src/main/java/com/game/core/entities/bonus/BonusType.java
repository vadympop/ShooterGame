package com.game.core.entities.bonus;

import java.security.SecureRandom;
import java.util.List;

public enum BonusType {
    SPEED_BOOST,
    DOUBLE_DAMAGE,
    DOUBLE_SHOOT,
    CIRCULAR_SHOOT,
    SHIELD;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<BonusType> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();

    public static BonusType randomBonus()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
