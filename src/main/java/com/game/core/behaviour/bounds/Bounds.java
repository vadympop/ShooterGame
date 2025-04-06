package com.game.core.behaviour.bounds;


import com.game.core.behaviour.interfaces.Positionable;

public interface Bounds extends Positionable {
    boolean intersects(Bounds otherBounds);
    boolean contains(Bounds otherBounds);
    float getMaxX(float x);
    float getMaxY(float y);
    float getMinX(float x);
    float getMinY(float y);
}
