package com.game.gui.views.game;

import com.game.core.utils.SceneLoader;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URISyntaxException;

public class GameView extends Application {
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;

    private GraphicsContext gc;
    private GameScene gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    public static double calculateScale() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        return Math.min(screenWidth / WIDTH, screenHeight / HEIGHT);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        primaryStage.setTitle("2D Shooter Game");
        // primaryStage.setResizable(true);
        // primaryStage.initStyle(StageStyle.UNDECORATED);

        SceneConfig config = ConfigLoader.getInstance().load("001");

        Canvas canvas = new Canvas(config.getSceneWidth(), config.getSceneHeight());
        gc = canvas.getGraphicsContext2D();

        System.out.println("0000");

        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneLoader loader = new SceneLoader(config);
        gameScene = loader.loadScene();

        System.out.println("1111");

        gameScene.start();
        startGameLoop();
    }

    private void startGameLoop() {
        new AnimationTimer() {
            private long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(deltaTime);
                render();
            }
        }.start();
    }

    private void update(double deltaTime) {
        gameScene.update(deltaTime);
    }

    private void render() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gameScene.render(gc);
    }
}
