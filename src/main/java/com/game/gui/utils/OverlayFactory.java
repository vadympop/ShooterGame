package com.game.gui.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public final class OverlayFactory {
    private OverlayFactory() {}

    public static VBox createPauseOverlay(Runnable togglePauseCallback, Runnable loadMainMenuCallback) {
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> togglePauseCallback.run());

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> loadMainMenuCallback.run());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(20, resumeButton, mainMenuButton, exitButton);
        menu.setAlignment(Pos.CENTER);
        FXUtils.styleOverlay(menu);
        StackPane.setAlignment(menu, Pos.CENTER);

        menu.setVisible(false);
        return menu;
    }

    public static Pane createGameEndOverlay(Runnable restartCallback, Runnable loadMainMenuCallback) {
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartCallback.run());

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> loadMainMenuCallback.run());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        VBox content = new VBox(20, restartButton, mainMenuButton, exitButton);
        content.setAlignment(Pos.CENTER);
        StackPane.setAlignment(content, Pos.CENTER);

        StackPane menu = new StackPane(content);
        FXUtils.styleOverlay(menu);

        menu.setVisible(false);
        return menu;
    }
}