package com.game.core.behaviour.bounds;


import com.game.core.behaviour.base.PositionWrapper;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.Scaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RectangleBounds extends PositionWrapper implements Bounds {
    private static final Logger LOGGER = LoggerFactory.getLogger(RectangleBounds.class);

    private float height;
    private float width;
    private float multiplier = 1f;

    public RectangleBounds(float width, float height) {
        Scaler scaler = Scaler.getInstance();
        setHeight(height * scaler.getScale());
        setWidth(width * scaler.getScale());
    }

    public RectangleBounds(float width, float height, float scale) {
        setHeight(height * scale);
        setWidth(width * scale);
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
            float closestY = Math.max(curY - curHHeight, Math.min(otherY, curY + curHHeight));
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

    @Override public float getMaxX() { return getX() + (getWidth() / 2); }
    @Override public float getMaxY() { return getY() + (getHeight() / 2); }
    @Override public float getMinX() { return getX() - (getWidth() / 2); }
    @Override public float getMinY() { return getY() - (getHeight() / 2); }

    @Override
    public float getMaxSize() {
        return (float) Math.hypot(getWidth(), getHeight());
    }

    @Override
    public void multiply(float multiplier) {
        if (multiplier <= 0) throw new InvalidParameterException("Multiplier must be higher than 0");

        LOGGER.debug("Rect bounds {} multiplied by {}", this, multiplier);
        this.multiplier = multiplier;
    }

    @Override
    public Bounds copy() {
        return new RectangleBounds(getWidth(), getHeight());
    }

    public float getHeight() { return this.multiplier * this.height; }
    private void setHeight(float height) {
        if (height <= 0) throw new InvalidParameterException("Rect bounds height must be higher than 0");

        this.height = height;
    }

    public float getWidth() { return this.multiplier * this.width; }
    private void setWidth(float width) {
        if (width <= 0) throw new InvalidParameterException("Rect bounds width must be higher than 0");

        this.width = width;
    }

    @Override
    public String toString() {
        return "RectangleBounds{" +
                "height=" + height +
                ", width=" + width +
                ", multiplier=" + multiplier +
                "}->" + super.toString();
    }
}
