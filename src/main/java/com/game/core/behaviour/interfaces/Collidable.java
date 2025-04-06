package com.game.core.behaviour.interfaces;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.collisions.CollisionVisitor;

public interface Collidable {
    void onCollision(CollisionVisitor visitor, Collidable other);
    Bounds getHitbox();
    void setHitbox(Bounds hitbox);
}
