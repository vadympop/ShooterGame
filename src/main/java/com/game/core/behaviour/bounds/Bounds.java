package com.game.core.behaviour.bounds;


import com.game.core.behaviour.interfaces.Positionable;

public interface Bounds {
    boolean intersects(Bounds checkedBounds, Positionable currentBoundsPos, Positionable checkedPos);
    boolean contains(Positionable currentBoundsPos, Positionable checkedPos);
}
