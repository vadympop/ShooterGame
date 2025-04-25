package com.game.core.behaviour.base;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.bounds.Bounds;

import java.util.Objects;

public abstract class CollidableGameObject extends GameObject implements Positionable, Collidable {
    private Bounds hitbox;

    /**
     * Constructs a new collidable game object with the specified hitbox.
     *
     * @param hitbox the bounding box representing the collision area of the object
     */
    public CollidableGameObject(Bounds hitbox) {
        setHitbox(hitbox);
    }

    /**
     * Checks if this object's hitbox fully contains the hitbox of the specified
     * collidable game object.
     *
     * @param obj the collidable game object to check against
     * @return true if this object's hitbox contains the other object's hitbox, false otherwise
     */
    public boolean contains(CollidableGameObject obj) {
        return this.getHitbox().contains(obj.getHitbox());
    }

    /**
     * Determines if this object's hitbox intersects with the hitbox of the specified
     * collidable game object.
     *
     * @param obj the collidable game object to check against
     * @return true if the hitboxes intersect, false otherwise
     */
    public boolean intersects(CollidableGameObject obj) {
        return this.getHitbox().intersects(obj.getHitbox());
    }

    /**
     * Sets the position of this game object and adjusts the position of its hitbox accordingly.
     *
     * @param x the new x-coordinate of the object
     * @param y the new y-coordinate of the object
     */
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
