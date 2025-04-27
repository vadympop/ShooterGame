package com.game.gui.scenes;

import com.game.core.utils.Scaler;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.scenes.game.GameScene;
import com.game.gui.scenes.menu.MenuScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Manages the lifecycle and transitions of scenes in the application.
 * It handles loading, unloading, and switching between game and menu scenes.
 */
public class SceneManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);

    private Scene currentScene;
    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        setPrimaryStage(primaryStage);
    }

    /**
     * Loads and displays the game scene identified by the given scene ID.
     * Exits the current scene (if any) before transitioning to the new scene.
     *
     * @param sceneId the ID of the scene to load
     */
    public void loadGameScene(String sceneId) {
        LOGGER.info("Loading game scene with id {}", sceneId);
        if (getCurrentScene() != null) getCurrentScene().onExit();

        SceneConfig config;
        try {
            config = ConfigManager.getInstance().loadSceneConfig(sceneId);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Scaler scaler = Scaler.getInstance();
        scaler.setSettings(config);

        setCurrentScene(new GameScene(getPrimaryStage(), this));
        getCurrentScene().onEnter();
    }

    /**
     * Loads and displays the menu scene.
     * Exits the current scene (if any) before transitioning to the menu scene.
     */
    public void loadMenuScene() {
        LOGGER.info("Loading menu scene");
        if (getCurrentScene() != null) getCurrentScene().onExit();

        setCurrentScene(new MenuScene(getPrimaryStage(), this));
        getCurrentScene().onEnter();
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message the error message to display
     */
    public void throwError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the currently active scene.
     *
     * @return the current scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Sets the currently active scene.
     *
     * @param currentScene the scene to set as active
     */
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }
}
