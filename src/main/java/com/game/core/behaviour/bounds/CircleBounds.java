package com.game.core.behaviour.bounds;

import com.game.core.behaviour.interfaces.Positionable;

public class CircleBounds implements Bounds {
    private float radius;

    public CircleBounds(float radius) {
        this.setRadius(radius);
    }

    @Override
    public boolean intersects(Bounds checkedBounds, Positionable currentBoundsPos, Positionable checkedPos) {
        if (other instanceof CircleBounds circle) {
            float x = this.getX();
            float y = this.getY();
            return Math.sqrt(Math.pow(x - circle.getX(), 2) + Math.pow(y - circle.getY(), 2)) <= this.getRadius() + circle.getRadius();
        } else if (other instanceof RectangleBounds) {
            return other.intersects(this);
        }

        return false;
    }

    @Override
    public boolean contains(Positionable currentBoundsPos, Positionable checkedPos) {
        return false;
    }

    public float getRadius() {
        return this.radius;
    }

    private void setRadius(float radius) {
        this.radius = radius;
    }
}
