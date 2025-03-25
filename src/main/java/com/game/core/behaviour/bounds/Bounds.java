package com.game.core.behaviour.bounds;


import com.game.core.behaviour.interfaces.Positionable;

public interface Bounds {
    boolean intersects(Bounds otherBounds, Positionable curPos, Positionable otherPos);
    boolean contains(Bounds otherBounds, Positionable curPos, Positionable otherPos);
}
