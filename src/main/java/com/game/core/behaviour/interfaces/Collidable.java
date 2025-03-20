package com.game.core.behaviour.interfaces;

import com.game.core.behaviour.Bounds;
import com.game.core.managers.CollisionVisitor;

public interface Collidable {
    void onCollision(CollisionVisitor visitor, Collidable other);
    Bounds getBounds();
    void setBounds(Bounds bounds);
    boolean contains(float x, float y);
}
