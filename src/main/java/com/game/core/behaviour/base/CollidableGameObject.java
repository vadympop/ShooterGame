package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.bounds.Bounds;

public abstract class CollidableGameObject extends GameObject implements Positionable, Collidable {
    private Bounds bounds;

    public boolean contains(CollidableGameObject obj) {
        return this.getBounds().contains(obj.getBounds(), this, obj);
    }

    public boolean intersects(CollidableGameObject obj) {
        return this.getBounds().intersects(obj.getBounds(), this, obj);
    }

    @Override public Bounds getBounds() { return bounds; }
    public void setBounds(Bounds bounds) { this.bounds = bounds; }
}
