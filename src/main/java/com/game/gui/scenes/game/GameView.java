package com.game.gui.scenes.game;

import com.game.core.entities.Entity;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.Scaler;
import com.game.gui.utils.FXUtils;
import com.game.gui.utils.RenderUtils;
import com.game.gui.utils.TimeUtils;
import com.game.gui.utils.WindowUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import static com.game.gui.scenes.menu.MenuViewConstants.*;

public class GameView {
    private static final Image PAUSE_BTN_IMAGE = new Image("/images/pause_btn.png");

    private static final int PAUSE_BTN_SIZE = 32;

    private GraphicsContext gc;
    private Stage primaryStage;
    private GameController controller;

    private HBox hud;
    private Label timerLabel;
    private VBox pauseOverlay;
    private Pane gameEndOverlay;
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

    private void resetPlayerControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyPressed(null);
    }

    public void render(
            List<SceneTile> backgroundTiles,
            List<Spawner> spawners,
            List<Block> blocks,
            List<Entity> entities,
            List<SceneTile> overlayTiles,
            List<PlayerSpawner> playerSpawners
    ) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        Stream.of(
                backgroundTiles,
                spawners,
                blocks
        ).forEach(list -> list.forEach(x -> x.draw(gc)));

        renderPlayerKills(playerSpawners);

        Stream.of(
                entities,
                overlayTiles
        ).forEach(list -> list.forEach(x -> x.draw(gc)));

        updateGUI();
    }

    private void renderPlayerKills(List<PlayerSpawner> spawners) {
        for (PlayerSpawner spawner : spawners) {
            double x = spawner.getX();
            double y = spawner.getY();
            double ovalWidth = 25;
            double ovalHeight = 25;
            int killsCount = spawner.getPlayerKillsCount();
            String killsText = String.valueOf(killsCount);

            RenderUtils.drawTextInOval(gc, x, y, ovalWidth, ovalHeight, killsText, killsCount != 0);
        }
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

        gameEndOverlay = createGameEndOverlay();
        gameEndOverlay.setVisible(false);
        root.getChildren().add(gameEndOverlay);
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
        if (controller.getMainTimer() != null) {
            long time = Math.round(controller.getMainTimer().getTimeLeft());
            timerLabel.setText(TimeUtils.formatTime(time));
        }
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

    public void gameEnd(PlayerSpawner winner) {
        resetPlayerControls(getPrimaryStage().getScene());
        Text winnerLabel = createWinnerLabel(winner.getX(), winner.getY());

        gameEndOverlay.getChildren().add(winnerLabel);
        gameEndOverlay.setVisible(true);
    }

    private Pane createGameEndOverlay() {
        Pane overlay = new Pane();
        overlay.setPadding(new Insets(30));
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.5)");

        return overlay;
    }

    private Text createWinnerLabel(float spawnerX, float spawnerY) {
        double offset = 50;
        double textX, textY, angle;

        boolean isLeft = spawnerX < WINDOW_WIDTH / 2.0;
        boolean isTop = spawnerY < WINDOW_HEIGHT / 2.0;

        if (isLeft && isTop) {
            // Top left
            textX = spawnerX + offset;
            textY = spawnerY + offset;
            angle = 135;
        } else if (!isLeft && isTop) {
            // Top right
            textX = spawnerX - offset;
            textY = spawnerY + offset;
            angle = 45;
        } else if (isLeft && !isTop) {
            // Bottom left
            textX = spawnerX + offset;
            textY = spawnerY - offset;
            angle = 45;
        } else {
            // Bottom right
            textX = spawnerX - offset;
            textY = spawnerY - offset;
            angle = 135;
        }

        Text label = new Text("Winner");

        label.setStrokeWidth(4);
        label.setStroke(Color.web("#29005c"));
        label.setFont(Font.font("Impact", FontWeight.EXTRA_BOLD, 60));

        double textWidth = label.getLayoutBounds().getWidth();
        double textHeight = label.getLayoutBounds().getHeight();

        label.setLayoutX(textX - textWidth / 2.0);
        label.setLayoutY(textY - textHeight / 2.0);
        label.setRotate(angle);

        Timeline blinkAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(label.fillProperty(), Color.WHITE)),
                new KeyFrame(Duration.millis(200), new KeyValue(label.fillProperty(), Color.web("#e0da2f"))),
                new KeyFrame(Duration.millis(200 * 2), new KeyValue(label.fillProperty(), Color.WHITE))
        );
        blinkAnimation.setCycleCount(Timeline.INDEFINITE);
        blinkAnimation.play();

        return label;
    }

    private void togglePauseMenu() {
        controller.togglePause();
        pauseOverlay.setVisible(!pauseOverlay.isVisible());
    }

    public Stage getPrimaryStage() { return primaryStage; }
    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setController(GameController controller) { this.controller = controller; }
}
