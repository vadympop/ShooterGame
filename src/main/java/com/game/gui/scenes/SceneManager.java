package com.game.gui.scenes;

import com.game.core.utils.Scaler;
import com.game.core.utils.GameSceneLoader;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.scenes.game.GameModel;
import com.game.gui.scenes.game.GameScene;
import com.game.gui.scenes.menu.MenuScene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class SceneManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneManager.class);

    private double xOffset;
    private double yOffset;

    private Scene currentScene;
    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        setPrimaryStage(primaryStage);
    }

    public void makeWindowMovable(Node root) {
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent ->  {
            primaryStage.setX(mouseEvent.getScreenX() - xOffset);
            primaryStage.setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    public void loadGameScene(String sceneId) {
        LOGGER.info("Loading game scene with id {}", sceneId);
        if (getCurrentScene() != null) getCurrentScene().onExit();

        SceneConfig config;
        try {
            config = ConfigLoader.getInstance().load(sceneId);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Scaler scaler = Scaler.getInstance();
        scaler.setSettings(config);

        GameSceneLoader loader = new GameSceneLoader(config, scaler);
        GameModel model = loader.loadScene();

        setCurrentScene(new GameScene(model, getPrimaryStage(), this));
        getCurrentScene().onEnter();
    }

    public void loadMenuScene() {
        LOGGER.info("Loading menu scene");
        if (getCurrentScene() != null) getCurrentScene().onExit();

        setCurrentScene(new MenuScene(getPrimaryStage(), this));
        getCurrentScene().onEnter();
    }

    public void throwError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getCurrentScene() { return currentScene; }
    public void setCurrentScene(Scene currentScene) { this.currentScene = currentScene; }

    public Stage getPrimaryStage() { return primaryStage; }
    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }
}
