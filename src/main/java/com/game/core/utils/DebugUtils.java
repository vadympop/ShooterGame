package com.game.core.utils;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.config.ConfigLoader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugUtils.class);

    private static final Color HITBOX_COLOR = Color.AQUAMARINE;

    public static void drawHitbox(GraphicsContext gc, Bounds hitbox) {
        gc.setStroke(HITBOX_COLOR);
        gc.setLineWidth(1.5);

        if (hitbox instanceof CircleBounds circHitbox) {
            float d = circHitbox.getRadius() * 2;
            double displayX = circHitbox.getX() - (d / 2);
            double displayY = circHitbox.getY() - (d / 2);

            gc.strokeOval(displayX, displayY, d, d);
        } else if (hitbox instanceof RectangleBounds rectHitbox) {
            float width = rectHitbox.getWidth();
            float height = rectHitbox.getHeight();
            double displayX = rectHitbox.getX() - (width / 2);
            double displayY = rectHitbox.getY() - (height / 2);

            gc.strokeRect(displayX, displayY, width, height);
        } else {
            LOGGER.debug("Not found drawing hitboxes logic for {}", hitbox.getClass().getSimpleName());
        }
    }

    public static void drawHitboxIfDebug(GraphicsContext gc, Bounds hitbox) {
        if (!ConfigLoader.getInstance().getConfig().isDebug()) return;
        drawHitbox(gc, hitbox);
    }
}
