package com.game.gui.views.game;

import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.utils.Scaler;
import com.game.core.utils.SceneLoader;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.GameLoop;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;

public class GameView extends Application {
    private GraphicsContext gc;
    private GameScene gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        primaryStage.setTitle("2D Shooter Game");

        SceneConfig config = ConfigLoader.getInstance().load("001");
        Scaler scaler = Scaler.getInstance();
        scaler.setSettings(config);

        Canvas canvas = new Canvas(scaler.getSceneWidth(), scaler.getSceneHeight());
        gc = canvas.getGraphicsContext2D();

        Pane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, scaler.getSceneWidth(), scaler.getSceneHeight());
        scene.setFill(Color.BLACK);

        primaryStage.setMinHeight(scaler.getSceneHeight());
        primaryStage.setMinWidth(scaler.getSceneWidth());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        SceneLoader loader = new SceneLoader(config, scaler);
        gameScene = loader.loadScene();

        String[] keys = {"W", "UP", "SPACE", "BACK_SPACE"};
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.W) {
                    PlayerSpawner s = (PlayerSpawner) gameScene.getSpawners().getFirst();
                    s.getPlayer().onKeyPressed();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.W) {
                    PlayerSpawner s = (PlayerSpawner) gameScene.getSpawners().getFirst();
                    s.getPlayer().onKeyReleased();
                }
            }
        });


        gameScene.start();
        new GameLoop(this::render, this::update).start();
    }

    private void update(double deltaTime) {
        gameScene.update(deltaTime);
    }

    private void render() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gameScene.render(gc);
    }
}
