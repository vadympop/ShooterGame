package com.game.core.behaviour.bounds;

import com.game.core.behaviour.base.PositionWrapper;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.Scaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents circular bounds for game objects which can intersect or contain other bounds.
 * This class extends {@link PositionWrapper} and implements {@link Bounds}.
 */
public class CircleBounds extends PositionWrapper implements Bounds {
    private static final Logger LOGGER = LoggerFactory.getLogger(CircleBounds.class);

    private float radius;
    private float multiplier = 1f;

    /**
     * Constructs a new {@code CircleBounds} with a scaled radius.
     *
     * @param radius the original radius before scaling.
     */
    public CircleBounds(float radius) {
        Scaler scaler = Scaler.getInstance();
        setRadius(radius * scaler.getScale());
    }

    /**
     * Constructs a new {@code CircleBounds} with a specified radius and scaling factor.
     *
     * @param radius the original radius.
     * @param scale  the scaling factor to adjust the radius.
     */
    public CircleBounds(float radius, float scale) {
        setRadius(radius * scale);
    }

    /**
     * Checks if this circular bounds intersects with another bounds.
     *
     * @param otherBounds the bounds to check intersection with.
     * @return {@code true} if this bounds intersects with {@code otherBounds}, {@code false} otherwise.
     */
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

    /**
     * Checks if this circular bounds fully contains another bounds.
     *
     * @param otherBounds the bounds to check containment of.
     * @return {@code true} if this bounds contains {@code otherBounds}, {@code false} otherwise.
     */
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

    /**
     * Gets the maximum X-coordinate of this circular bounds.
     *
     * @return the maximum X-coordinate.
     */
    @Override
    public float getMaxX() {
        return getX() + getRadius();
    }

    /**
     * Gets the maximum Y-coordinate of this circular bounds.
     *
     * @return the maximum Y-coordinate.
     */
    @Override
    public float getMaxY() {
        return getY() + getRadius();
    }

    /**
     * Gets the minimum X-coordinate of this circular bounds.
     *
     * @return the minimum X-coordinate.
     */
    @Override
    public float getMinX() {
        return getX() - getRadius();
    }

    /**
     * Gets the minimum Y-coordinate of this circular bounds.
     *
     * @return the minimum Y-coordinate.
     */
    @Override
    public float getMinY() {
        return getY() - getRadius();
    }

    /**
     * Gets the maximum size (diameter) of this circular bounds.
     *
     * @return the maximum size (diameter).
     */
    @Override
    public float getMaxSize() {
        return getRadius() * 2;
    }

    /**
     * Multiplies the radius of the circle by a given multiplier.
     *
     * @param multiplier the factor to multiply the radius by.
     * @throws InvalidParameterException if {@code multiplier} is less than or equal to 0.
     */
    @Override
    public void multiply(float multiplier) {
        if (multiplier <= 0) throw new InvalidParameterException("Multiplier must be higher than 0");

        LOGGER.debug("Circl Bounds {} multiplied by {}", this, multiplier);
        this.multiplier = multiplier;
    }

    @Override
    public Bounds copy() {
        return new CircleBounds(getRadius());
    }

    public float getRadius() { return this.multiplier * this.radius; }
    private void setRadius(float radius) {
        if (radius <= 0) throw new InvalidParameterException("Circle bounds radius must be higher than 0");
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "CircleBounds{" +
                "radius=" + radius +
                ", multiplier=" + multiplier +
                "}->" + super.toString();
    }
}
