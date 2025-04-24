package com.game.gui.utils;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public final class RenderUtils {
    private RenderUtils() {}

    public static void drawTextInOval(
            GraphicsContext gc,
            double x,
            double y,
            double width,
            double height,
            String text,
            boolean isActive
    ) {
        gc.setFill(isActive ? Color.BLACK : Color.GRAY);
        gc.fillOval(x, y, width, height);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(x, y, width, height);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        double centerX = x + width / 2.0;
        double centerY = y + height / 2.0;
        gc.fillText(text, centerX, centerY);
    }
}
