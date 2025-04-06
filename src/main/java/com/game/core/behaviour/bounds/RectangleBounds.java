package com.game.core.behaviour.bounds;


import com.game.core.behaviour.base.Position;

public class RectangleBounds extends Position implements Bounds {
    private float height;
    private float width;

    public RectangleBounds(float width, float height) {
        setHeight(height);
        setWidth(width);
    }

    @Override
    public boolean intersects(Bounds otherBounds) {
        float curX = getX();
        float curY = getY();
        float otherX = otherBounds.getX();
        float otherY = otherBounds.getY();
        float curHWidth = getWidth() / 2;
        float curHHeight = getHeight() / 2;

        if (otherBounds instanceof RectangleBounds rect) {
            float otherHWidth = rect.getWidth() / 2;
            float otherHHeight = rect.getHeight() / 2;

            return Math.abs(curX - otherX) < (curHWidth + otherHWidth) &&
                    Math.abs(curY - otherY) < (curHHeight + otherHHeight);
        } else if (otherBounds instanceof CircleBounds circle) {
            float closestX = Math.max(curX - curHWidth, Math.min(otherX, curX + curHWidth));
            float closestY = Math.max(curY + curHHeight, Math.min(otherY, curY - curHHeight));
            return Math.hypot(otherX - closestX, otherY - closestY) <= circle.getRadius();
        }

        return false;
    }

    @Override
    public boolean contains(Bounds otherBounds) {
        float curX = getX();
        float curY = getY();
        float otherX = otherBounds.getX();
        float otherY = otherBounds.getY();
        float curHWidth = getWidth() / 2;
        float curHHeight = getHeight() / 2;

        if (otherBounds instanceof CircleBounds circle) {
            return Math.abs(otherX - curX) + circle.getRadius() <= curHWidth &&
                    Math.abs(otherY - curY) + circle.getRadius() <= curHHeight;
        } else if (otherBounds instanceof RectangleBounds rect) {
            return Math.abs(curX - otherX) + (rect.getWidth() / 2) <= curHWidth &&
                    Math.abs(curY - otherY) + (rect.getHeight() / 2) <= curHHeight;
        }

        return false;
    }

    @Override public float getMaxX(float x) { return x + (getWidth() / 2); }
    @Override public float getMaxY(float y) { return y + (getHeight() / 2); }
    @Override public float getMinX(float x) { return x - (getWidth() / 2); }
    @Override public float getMinY(float y) { return y - (getHeight() / 2); }

    public float getHeight() { return this.height; }
    private void setHeight(float height) { this.height = height; }

    public float getWidth() { return this.width; }
    private void setWidth(float width) { this.width = width; }
}
