package com.game.core.behaviour.bounds;

import com.game.core.behaviour.interfaces.Positionable;

public class CircleBounds implements Bounds {
    private float radius;

    public CircleBounds(float radius) {
        this.setRadius(radius);
    }

    @Override
    public boolean intersects(Bounds checkedBounds, Positionable currentPos, Positionable checkedPos) {
        if (checkedBounds instanceof CircleBounds circle) {
            float curX = currentPos.getX();
            float curY = currentPos.getY();
            float checkedX = checkedPos.getX();
            float checkedY = checkedPos.getY();

            return Math.sqrt(Math.pow(curX - checkedX, 2) + Math.pow(curY - checkedY, 2)) <= this.getRadius() + circle.getRadius();
        } else if (checkedBounds instanceof RectangleBounds) {
            return checkedBounds.intersects(this, checkedPos, currentPos);
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
