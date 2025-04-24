package com.game.gui.scenes.game;

import com.game.core.entities.Entity;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.TileType;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.Scaler;
import com.game.gui.utils.FXUtils;
import com.game.gui.utils.TimeUtils;
import com.game.gui.utils.WindowUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Stream;

public class GameView {
    private static final Image PAUSE_BTN_IMAGE = new Image("/images/pause_btn.png");

    private static final int PAUSE_BTN_SIZE = 32;

    private GraphicsContext gc;
    private Stage primaryStage;
    private GameController controller;

    private HBox hud;
    private Label timerLabel;
    private VBox pauseOverlay;
    private Pane root;

    public GameView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        init();
    }

    public void init() {
        Stage primaryStage = getPrimaryStage();
        Scaler scaler = Scaler.getInstance();

        Canvas canvas = new Canvas(scaler.getSceneWidth(), scaler.getSceneHeight());
        gc = canvas.getGraphicsContext2D();

        root = new StackPane();
        root.getChildren().add(canvas);

        createGUI();

        Scene scene = new Scene(root, scaler.getSceneWidth(), scaler.getSceneHeight());
        scene.setFill(Color.BLACK);
        setPlayerControls(scene);

        primaryStage.setTitle("2D Shooter Game");
        primaryStage.setMinHeight(scaler.getSceneHeight());
        primaryStage.setMinWidth(scaler.getSceneWidth());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    private void setPlayerControls(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> controller.onKeyPressed(keyEvent));
        scene.setOnKeyReleased(keyEvent -> controller.onKeyReleased(keyEvent));
    }

    public void render(
            List<SceneTile> backgroundTiles,
            List<Spawner> spawners,
            List<Block> blocks,
            List<Entity> entities,
            List<SceneTile> overlayTiles
    ) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        Stream.of(
                backgroundTiles,
                spawners,
                blocks,
                entities,
                overlayTiles
        ).forEach(list -> list.forEach(x -> x.draw(gc)));

        updateGUI();
    }

    public void show() {
        WindowUtils.makeWindowMovable(primaryStage, hud);
        primaryStage.show();
    }

    private void createGUI() {
        hud = createHUD();
        StackPane.setAlignment(hud, Pos.TOP_CENTER);
        root.getChildren().add(hud);

        pauseOverlay = createPauseOverlay();
        pauseOverlay.setVisible(false);
        root.getChildren().add(pauseOverlay);
    }

    private HBox createHUD() {
        HBox hud = new HBox();
        hud.setPadding(new Insets(10));

        timerLabel = new Label("00:00");
        timerLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setPadding(new Insets(10, 40, 10, 40));
        timerLabel.setStyle("-fx-background-color: #000; -fx-background-radius: 20; -fx-border-width: 4; -fx-border-radius: 15; -fx-border-color: #fff");

        ImageView pauseButton = FXUtils.createImageButton(PAUSE_BTN_IMAGE, PAUSE_BTN_SIZE, this::togglePauseMenu);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        hud.getChildren().addAll(spacer1, timerLabel, spacer2, pauseButton);
        hud.setMaxHeight(timerLabel.getHeight());
        return hud;
    }

    private void updateGUI() {
        long time = Math.round(controller.getMainTimer().getTimeLeft());
        timerLabel.setText(TimeUtils.formatTime(time));
    }

    private VBox createPauseOverlay() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(30));
        menu.setStyle("-fx-background-color: rgba(0,0,0,0.5)");

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> togglePauseMenu());

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> controller.loadMainMenu());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        menu.getChildren().addAll(resumeButton, mainMenuButton, exitButton);

        StackPane.setAlignment(menu, Pos.CENTER);
        return menu;
    }

    private void togglePauseMenu() {
        controller.togglePause();
        pauseOverlay.setVisible(!pauseOverlay.isVisible());
    }

    public Stage getPrimaryStage() { return primaryStage; }
    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setController(GameController controller) { this.controller = controller; }
}
