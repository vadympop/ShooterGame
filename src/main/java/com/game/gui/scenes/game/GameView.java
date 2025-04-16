package com.game.gui.scenes.game;

import com.game.core.scene.graphics.TileType;
import com.game.core.utils.Scaler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.stream.Stream;

public class GameView {
    private GraphicsContext gc;
    private Stage primaryStage;
    private GameController controller;

    public GameView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        init();
    }

    public void init() {
        Stage primaryStage = getPrimaryStage();
        Scaler scaler = Scaler.getInstance();

        Canvas canvas = new Canvas(scaler.getSceneWidth(), scaler.getSceneHeight());
        gc = canvas.getGraphicsContext2D();

        Pane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, scaler.getSceneWidth(), scaler.getSceneHeight());
        scene.setFill(Color.BLACK);
        setKeyListeners(scene);

        primaryStage.setTitle("2D Shooter Game");
        primaryStage.setMinHeight(scaler.getSceneHeight());
        primaryStage.setMinWidth(scaler.getSceneWidth());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
    }

    private void setKeyListeners(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> controller.onKeyPressed(keyEvent));
        scene.setOnKeyReleased(keyEvent -> controller.onKeyReleased(keyEvent));
    }

    private void drawTilesByType(TileType type) {
        controller.getModel().getTilesByType(type)
                .forEach(tile -> tile.draw(gc));
    }

    public void render() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        drawTilesByType(TileType.BACKGROUND);

        GameModel model = controller.getModel();
        Stream.of(
                model.getSpawners(),
                model.getBlocks(),
                model.getEntities()
        ).forEach(list -> list.forEach(x -> x.draw(gc)));

        drawTilesByType(TileType.OVERLAY);
    }

    public void show() {
        primaryStage.show();
    }

    public Stage getPrimaryStage() { return primaryStage; }
    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setController(GameController controller) { this.controller = controller; }
}
