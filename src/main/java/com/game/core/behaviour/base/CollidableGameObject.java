package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.bounds.Bounds;

public abstract class CollidableGameObject extends GameObject implements Positionable, Collidable {
    private Bounds bounds;

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }

    protected void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public boolean contains(Positionable checkedObj) {
        return this.getBounds().contains(this, checkedObj);
    }

    public boolean intersects(CollidableGameObject obj) {
        return this.getBounds().intersects(obj.getBounds(), this, obj);
    }
}
