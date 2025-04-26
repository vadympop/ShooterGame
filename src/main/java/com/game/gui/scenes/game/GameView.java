package com.game.gui.scenes.game;

import com.game.core.entities.Entity;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.Scaler;
import com.game.gui.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Stream;


/**
 * The GameView class creates and manages the game HUD, pause menu, and end game overlay.
 * It handles rendering game elements, managing player controls, and responding to various events.
 */
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

    /**
     * Initializes the game view by setting up the primary stage, creating the canvas,
     * initializing the GUI components, and setting player controls.
     */
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

    /**
     * Configures the player controls by setting keyboard input handlers for the given scene.
     *
     * @param scene The scene where the player controls will be configured.
     */
    private void setPlayerControls(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> controller.onKeyPressed(keyEvent));
        scene.setOnKeyReleased(keyEvent -> controller.onKeyReleased(keyEvent));
    }

    /**
     * Resets the player controls by removing all input handlers from the given scene.
     *
     * @param scene The scene where the player controls will be reset.
     */
    private void resetPlayerControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyPressed(null);
    }

    /**
     * Renders the game elements on the canvas, including background tiles, spawners, blocks,
     * entities, and overlay tiles. Also updates the player's HUD and current state.
     *
     * @param backgroundTiles A list of tiles representing the background layer.
     * @param spawners        A list of spawners responsible for spawning entities in the scene.
     * @param blocks          A list of blocks used as obstacles or interactable objects in the game.
     * @param entities        A list of game entities to be drawn on the canvas.
     * @param overlayTiles    A list of tiles representing the overlay (UI) layer.
     * @param playerSpawners  A list of player spawners used for tracking kills and positions.
     */
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

    /**
     * Renders the number of player kills near each player's position.
     *
     * @param spawners A list of player spawners used to track player kills.
     */
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

    /**
     * Displays the primary stage of the game GUI and makes the window movable.
     */
    public void show() {
        WindowUtils.makeWindowMovable(primaryStage, hud);
        primaryStage.show();
    }

    /**
     * Restarts the game by hiding the end game overlay and resetting player controls.
     */
    public void restart() {
        gameEndOverlay.setVisible(false);
        gameEndOverlay.getChildren().removeLast(); // Remove winner label
        setPlayerControls(getPrimaryStage().getScene());
    }

    /**
     * Creates the graphical user interface components, including the HUD, pause menu,
     * and end game overlay.
     */
    private void createGUI() {
        hud = createHUD();
        StackPane.setAlignment(hud, Pos.TOP_CENTER);

        pauseOverlay = OverlayFactory.createPauseOverlay(this::togglePauseMenu, () -> controller.loadMainMenu());
        gameEndOverlay = OverlayFactory.createGameEndOverlay(() -> controller.restart(), () -> controller.loadMainMenu());

        root.getChildren().addAll(hud, pauseOverlay, gameEndOverlay);
    }

    /**
     * Creates the heads-up display (HUD), which includes the timer, pause button, and spacers.
     *
     * @return The HBox containing the HUD components.
     */
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

    /**
     * Updates the GUI elements, such as the timer display, based on the current game state.
     */
    private void updateGUI() {
        if (controller.getMainTimer() != null) {
            long time = Math.round(controller.getMainTimer().getTimeLeft());
            timerLabel.setText(TimeUtils.formatTime(time));
        }
    }

    /**
     * Displays the end game overlay with a label announcing the winner.
     *
     * @param winner The player spawner representing the winning player.
     */
    public void gameEnd(PlayerSpawner winner) {
        resetPlayerControls(getPrimaryStage().getScene());
        Text winnerLabel = createWinnerLabel(winner.getX(), winner.getY());

        gameEndOverlay.getChildren().add(winnerLabel);
        gameEndOverlay.setVisible(true);
    }

    /**
     * Creates a text label to indicate the winner, positioning it based on the player's coordinates.
     *
     * @param spawnerX The X-coordinate of the winning player spawner.
     * @param spawnerY The Y-coordinate of the winning player spawner.
     * @return A Text object styled and positioned to display the winner label.
     */
    private Text createWinnerLabel(float spawnerX, float spawnerY) {
        double offset = 50;
        double textX, textY, angle;

        Scaler scaler = Scaler.getInstance();

        double centerX = scaler.getSceneWidth() / 2.0;
        double centerY = scaler.getSceneHeight() / 2.0;
        boolean isLeft = spawnerX < centerX;
        boolean isTop = spawnerY < centerY;

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

        double translateX = textX - centerX;
        double translateY = textY - centerY;
        label.setTranslateX(translateX);
        label.setTranslateY(translateY);
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

    /**
     * Toggles the visibility of the pause menu and pauses or resumes the game.
     */
    private void togglePauseMenu() {
        controller.togglePause();
        pauseOverlay.setVisible(!pauseOverlay.isVisible());
    }

    public Stage getPrimaryStage() { return primaryStage; }
    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setController(GameController controller) { this.controller = controller; }
}
