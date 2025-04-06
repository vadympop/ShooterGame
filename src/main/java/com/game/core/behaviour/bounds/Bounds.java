package com.game.core.behaviour.bounds;


import com.game.core.behaviour.interfaces.Positionable;

public interface Bounds extends Positionable {
    boolean intersects(Bounds otherBounds);
    boolean contains(Bounds otherBounds);
    float getMaxX();
    float getMaxY();
    float getMinX();
    float getMinY();
    Bounds copy();
}
