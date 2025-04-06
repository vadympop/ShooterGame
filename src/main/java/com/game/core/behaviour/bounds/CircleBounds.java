package com.game.core.behaviour.bounds;

import com.game.core.behaviour.base.Position;

public class CircleBounds extends Position implements Bounds {
    private float radius;

    public CircleBounds(float radius) {
        setRadius(radius);
    }

    @Override
    public boolean intersects(Bounds otherBounds) {
        if (otherBounds instanceof CircleBounds circle) {
            float curX = getX();
            float curY = getY();
            float otherX = otherBounds.getX();
            float otherY = otherBounds.getY();

            return Math.hypot(curX - otherX, curY - otherY) < getRadius() + circle.getRadius();
        } else if (otherBounds instanceof RectangleBounds) {
            return otherBounds.intersects(this);
        }

        return false;
    }

    @Override
    public boolean contains(Bounds otherBounds) {
        float curX = getX();
        float curY = getY();
        float otherX = otherBounds.getX();
        float otherY = otherBounds.getY();

        if (otherBounds instanceof CircleBounds circle) {
            double distance = Math.hypot(otherX - curX, otherY - curY);
            return distance + circle.getRadius() <= getRadius();
        } else if (otherBounds instanceof RectangleBounds rect) {
            double dx = Math.abs(otherX - curX);
            double dy = Math.abs(otherY - curY);

            return Math.hypot(dx + (rect.getWidth() / 2), dy + (rect.getHeight() / 2)) <= getRadius();
        }

        return false;
    }

    @Override public float getMaxX(float x) { return x + getRadius(); }
    @Override public float getMaxY(float y) { return y + getRadius(); }
    @Override public float getMinX(float x) { return x - getRadius(); }
    @Override public float getMinY(float y) { return y - getRadius(); }

    public float getRadius() { return this.radius; }
    private void setRadius(float radius) { this.radius = radius; }
}
