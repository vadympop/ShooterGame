package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Positionable;

public abstract class GameObject extends PositionWrapper implements Positionable {
    @Override
    public String toString() {
        return "GameObject{}->" + super.toString();
    }
}
