package com.game.core.behaviour.base;


public abstract class PositionWrapper {
    private float x = 0;
    private float y = 0;

    public float getX() { return this.x; }
    public float getY() { return this.y; }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
