package com.game.gui.utils;

import javafx.scene.Node;
import javafx.stage.Stage;

public final class WindowUtils {
    private WindowUtils() {}

    /**
     * Makes a JavaFX window movable by dragging the specified node.
     *
     * @param stage the stage whose position will be changed
     * @param node  the node that will respond to mouse drag events
     */
    public static void makeWindowMovable(Stage stage, Node node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(event -> {
            dragDelta.x = event.getSceneX();
            dragDelta.y = event.getSceneY();
        });

        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - dragDelta.x);
            stage.setY(event.getScreenY() - dragDelta.y);
        });
    }

    private static class Delta {
        double x, y;
    }
}
