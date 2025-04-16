package com.game.gui;

import com.game.gui.scenes.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager sm = new SceneManager(primaryStage);
        sm.loadGameScene("001");
    }
}
