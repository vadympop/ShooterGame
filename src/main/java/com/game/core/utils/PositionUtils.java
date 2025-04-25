package com.game.core.utils;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.RectangleBounds;

/**
 * Utility class for position calculations and transformations.
 */
public class PositionUtils {
    /**
     * Generates a display position centered around the provided coordinates and the size of the given bounds.
     *
     * @param x    the x-coordinate of the original position
     * @param y    the y-coordinate of the original position
     * @param size the bounds of the object, used to calculate the display dimensions
     * @return a double array containing the centered display position as [displayX, displayY]
     */
    public static double[] generateDisplayPos(float x, float y, Bounds size) {
        float width, height;
        width = height = size.getMaxSize();
        if (size instanceof RectangleBounds rect) {
            width = rect.getWidth();
            height = rect.getHeight();
        }
        double displayX = x - (width / 2);
        double displayY = y - (height / 2);

        return new double[] {displayX, displayY};
    }
}
