package com.game.gui;

import com.game.gui.scenes.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Launching game");

        SceneManager sm = new SceneManager(primaryStage);
        sm.loadGameScene("001");
    }
}
