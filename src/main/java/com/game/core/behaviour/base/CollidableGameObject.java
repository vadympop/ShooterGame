package com.game.core.behaviour.base;

import com.game.core.behaviour.Bounds;
import com.game.core.behaviour.interfaces.Collidable;

public abstract class CollidableGameObject extends GameObject implements Collidable {
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
    public boolean contains(float x, float y) {
        return x >= getX() && x <= getX() + getBounds().getWidth() &&
                y >= getY() && y <= getY() + getBounds().getHeight();
    }
}
