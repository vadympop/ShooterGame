package com.game.gui.utils;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public final class FXUtils {
    private FXUtils() {}

    public static void onHoverOpacityChange(Node node, float baseOpacity, float newOpacity) {
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> node.setOpacity(newOpacity));
        node.addEventHandler(MouseEvent.MOUSE_EXITED, e -> node.setOpacity(baseOpacity));
    }
}
