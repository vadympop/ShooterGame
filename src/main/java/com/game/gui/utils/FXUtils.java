package com.game.gui.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public final class FXUtils {
    private FXUtils() {}

    public static ImageView createImageButton(Image image, double size, Runnable action) {
        ImageView btn = new ImageView(image);
        btn.setFitWidth(size);
        btn.setFitHeight(size);
        btn.setPreserveRatio(true);
        btn.setCursor(Cursor.HAND);
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        FXUtils.onHoverOpacityChange(btn, 0.5f, 1.0f);

        return btn;
    }

    public static void onHoverOpacityChange(Node node, float baseOpacity, float newOpacity) {
        node.setOpacity(baseOpacity);
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> node.setOpacity(newOpacity));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, e -> node.setOpacity(baseOpacity));
    }

    public static void styleOverlay(Pane node) {
        node.setPadding(new Insets(30));
        node.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
    }
}
