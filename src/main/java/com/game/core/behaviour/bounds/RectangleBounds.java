package com.game.core.behaviour.bounds;


import com.game.core.behaviour.interfaces.Positionable;

public class RectangleBounds implements Bounds {
    private float height;
    private float width;

    public RectangleBounds(float width, float height) {
        this.setHeight(height);
        this.setWidth(width);
    }

    @Override
    public boolean intersects(Bounds checkedBounds, Positionable currentPos, Positionable checkedPos) {
        float curX = currentPos.getX();
        float curY = currentPos.getY();
        float checkedX = checkedPos.getX();
        float checkedY = checkedPos.getY();

        if (checkedBounds instanceof RectangleBounds rect) {
            return Math.abs(curX - checkedX) < (this.getWidth() + rect.getWidth()) / 2 &&
                    Math.abs(curY - checkedY) < (this.getHeight() + rect.getHeight()) / 2;
        } else if (checkedBounds instanceof CircleBounds circle) {
            float closestX = Math.max(curX - this.getWidth() / 2, Math.min(checkedX, curX + this.getWidth() / 2));
            float closestY = Math.max(curY - this.getHeight() / 2, Math.min(checkedY, curY + this.getHeight() / 2));
            return Math.sqrt(Math.pow(checkedX - closestX, 2) + Math.pow(checkedY - closestY, 2)) <= circle.getRadius();
        }

        return false;
    }

    @Override
    public boolean contains(Positionable currentBoundsPos, Positionable checkedPos) {
        return false;
    }

    public float getHeight() {
        return this.height;
    }

    private void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    private void setWidth(float width) {
        this.width = width;
    }
}
