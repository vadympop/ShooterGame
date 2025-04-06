package com.game.core.behaviour.bounds;

import com.game.core.behaviour.base.PositionWrapper;

public class CircleBounds extends PositionWrapper implements Bounds {
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

    @Override public float getMaxX() { return getX() + getRadius(); }
    @Override public float getMaxY() { return getY() + getRadius(); }
    @Override public float getMinX() { return getX() - getRadius(); }
    @Override public float getMinY() { return getY() - getRadius(); }

    @Override
    public Bounds copy() {
        return new CircleBounds(getRadius());
    }

    public float getRadius() { return this.radius; }
    private void setRadius(float radius) { this.radius = radius; }
}
