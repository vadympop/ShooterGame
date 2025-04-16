package com.game.core.utils;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.RectangleBounds;

public class PositionUtils {
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
