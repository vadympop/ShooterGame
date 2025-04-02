package com.game.core.behaviour.bounds;

import com.game.core.behaviour.interfaces.Positionable;

public class CircleBounds implements Bounds {
    private float radius;

    public CircleBounds(float radius) {
        setRadius(radius);
    }

    @Override
    public boolean intersects(Bounds otherBounds, Positionable curPos, Positionable otherPos) {
        if (otherBounds instanceof CircleBounds circle) {
            float curX = curPos.getX();
            float curY = curPos.getY();
            float otherX = otherPos.getX();
            float otherY = otherPos.getY();

            return Math.hypot(curX - otherX, curY - otherY) < getRadius() + circle.getRadius();
        } else if (otherBounds instanceof RectangleBounds) {
            return otherBounds.intersects(this, otherPos, curPos);
        }

        return false;
    }

    @Override
    public boolean contains(Bounds otherBounds, Positionable curPos, Positionable otherPos) {
        float curX = curPos.getX();
        float curY = curPos.getY();
        float otherX = otherPos.getX();
        float otherY = otherPos.getY();

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

    public float getRadius() { return this.radius; }
    private void setRadius(float radius) { this.radius = radius; }
}
