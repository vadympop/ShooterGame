package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.bounds.Bounds;

import java.util.Objects;

public abstract class CollidableGameObject extends GameObject implements Positionable, Collidable {
    private Bounds hitbox;

    public CollidableGameObject(Bounds hitbox) {
        setHitbox(hitbox);
    }

    public boolean contains(CollidableGameObject obj) {
        return this.getHitbox().contains(obj.getHitbox());
    }

    public boolean intersects(CollidableGameObject obj) {
        return this.getHitbox().intersects(obj.getHitbox());
    }

    @Override
    public void setPos(float x, float y) {
        super.setPos(x, y);
        Bounds b = getHitbox();
        if (b != null) b.setPos(x, y);
    }

    @Override public Bounds getHitbox() { return hitbox; }
    public void setHitbox(Bounds hitbox) { this.hitbox = Objects.requireNonNull(hitbox); }

    @Override
    public String toString() {
        return "CollidableGameObject{" +
                "hitbox=" + hitbox +
                "}->" + super.toString();
    }
}
