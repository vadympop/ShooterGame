package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.Shape;

public abstract class CollidableGameObject implements Shape, Positionable, Collidable {
    private Bounds bounds;

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }

    @Override
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public float getX() {
        return this.getBounds().getX();
    }

    @Override
    public float getY() {
        return this.getBounds().getY();
    }

    @Override
    public void setPos(float x, float y) {
        this.getBounds().setPos(x, y);
    }

    @Override
    public boolean contains(float x, float y) {
        return this.getBounds().contains(x, y);
    }

    @Override
    public boolean intersects(Bounds other) {
        return this.getBounds().intersects(other);
    }
}
