package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Positionable;

public abstract class GameObject implements Positionable {
    private float x = 0;
    private float y = 0;

    @Override
    public float getX() { return this.x; }

    @Override
    public float getY() { return this.y; }

    @Override
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
