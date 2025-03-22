package com.game.core.behaviour.bounds;

public interface Shape {
    boolean intersects(Bounds other);
    boolean contains(float x, float y);
}