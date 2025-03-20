package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Positionable;

public abstract class GameObject implements Positionable {
    private float x;
    private float y;

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
}
