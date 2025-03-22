package com.game.core.behaviour.base;

public abstract class Coordinates {
    private float x;
    private float y;

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
