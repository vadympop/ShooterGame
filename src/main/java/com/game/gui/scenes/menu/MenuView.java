package com.game.gui.scenes.menu;

import com.game.core.enums.GameModeEnum;
import com.game.core.utils.config.ConfigManager;
import com.game.gui.scenes.menu.components.MapSelector;
import com.game.gui.scenes.menu.components.ScrollingBackground;
import com.game.gui.utils.FXUtils;
import com.game.gui.utils.WindowUtils;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.game.gui.scenes.menu.MenuViewConstants.*;

/**
 * Represents the main menu view of the game.
 */
public class MenuView {
    private static final Image TILE_IMAGE = new Image("/images/menu_bg_tile.png");
    private static final Image CARDS_IMAGE = new Image("/images/cards_bg.png");
    private static final Image LEFT_ARROW_IMAGE = new Image("/images/arrow_left.png");
    private static final Image RIGHT_ARROW_IMAGE = new Image("/images/arrow_right.png");

    private Stage primaryStage;
    private Pane dragArea;
    private MenuController controller;
    private ScrollingBackground background;

    public MenuView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        init();
    }

    /**
     * Initializes the main menu view, including setting up the UI components,
     * background animations, and adding necessary layout configurations.
     */
    public void init() {
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        startBackgroundAnimation(canvas.getGraphicsContext2D());

        VBox titleBox = createTitles();
        VBox controls = createScenesSelecter();
        Button exitButton = createExitButton();
        VBox content = createMainBody(titleBox, controls, exitButton);

        setupStage(canvas, content);
    }

    /**
     * Displays the menu view by making the primary stage visible
     * and enabling window drag functionality.
     */
    public void show() {
        WindowUtils.makeWindowMovable(primaryStage, dragArea);
        primaryStage.show();
    }

    /**
     * Creates the title section of the menu, containing the main title and subtitle.
     *
     * @return a VBox containing the title and subtitle labels
     */
    private VBox createTitles() {
        Label titleLabel = new Label("Multiplayer Shooter Minigame");
        titleLabel.setFont(Font.font(FONT_FAMILY, 48));
        titleLabel.setTextFill(Color.WHITE);

        Label titleSubLabel = new Label("This isn't that Tanks game!");
        titleSubLabel.setFont(Font.font(FONT_FAMILY, 20));
        titleSubLabel.setTextFill(Color.WHITE);

        VBox texts = new VBox(10, titleLabel, titleSubLabel);
        texts.setAlignment(Pos.CENTER);
        return texts;
    }

    /**
     * Creates the scene selector section of the menu, including a map selector, navigation arrows,
     * and a switch for selecting the game mode.
     *
     * @return a VBox containing the scene selector components
     */
    private VBox createScenesSelecter() {
        MapSelector mapSelector = new MapSelector(sceneId -> controller.changeGameScene(sceneId), SCENES);
        ImageView leftArrow = FXUtils.createImageButton(
                LEFT_ARROW_IMAGE, ARROW_SIZE, () -> mapSelector.scrollByAnimated(-1)
        );
        ImageView rightArrow = FXUtils.createImageButton(
                RIGHT_ARROW_IMAGE, ARROW_SIZE, () -> mapSelector.scrollByAnimated(1)
        );

        BackgroundSize bgSize = new BackgroundSize(
                BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false
        );
        BackgroundImage backgroundImage = new BackgroundImage(
                CARDS_IMAGE,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );

        ConfigManager cfgLoader = ConfigManager.getInstance();
        HBox gameModeSwitcher = createGameModeSwitcher(
                "Shooting with bullets reloading",
                "Infinity bullets mode",
                createCustomSwitch(
                        state -> {
                            if (state) cfgLoader.setGameMode(GameModeEnum.INFINITY_BULLETS);
                            else cfgLoader.setGameMode(GameModeEnum.WITH_RELOADING);
                        }
                )
        );

        HBox controls = new HBox(20, leftArrow, mapSelector.getScrollPane(), rightArrow);
        controls.setAlignment(Pos.CENTER);

        VBox scenesSelecter = new VBox(5, gameModeSwitcher, controls);
        scenesSelecter.setBackground(new Background(backgroundImage));
        scenesSelecter.setAlignment(Pos.CENTER);
        scenesSelecter.setPadding(new Insets(60));

        return scenesSelecter;
    }

    /**
     * Creates the exit button for the menu.
     * The button allows users to close the game.
     *
     * @return a styled Button configured with exit functionality
     */
    private Button createExitButton() {
        Button exitButton = new Button("Exit");
        exitButton.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        exitButton.setTextFill(Color.WHITE);
        exitButton.setStyle("-fx-background-color: #3b8948; -fx-background-radius: 3; -fx-padding: 10 30;");

        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: #53b966; -fx-background-radius: 3; -fx-padding: 10 30;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: #3b8948; -fx-background-radius: 3; -fx-padding: 10 30;"));
        exitButton.setOnAction(e -> primaryStage.close());
        return exitButton;
    }

    /**
     * Starts the background scrolling animation using the provided GraphicsContext.
     *
     * @param gc the GraphicsContext used to render the background animation
     */
    private void startBackgroundAnimation(GraphicsContext gc) {
        background = new ScrollingBackground(gc, TILE_IMAGE, WINDOW_WIDTH, WINDOW_HEIGHT);
        background.start();
    }

    /**
     * Creates the layout structure of the menu, combining the title, controls, and exit button.
     *
     * @param titleBox   the VBox containing the title section
     * @param controls   the VBox containing the controls such as the scene selector
     * @param exitButton the Button to exit the game
     * @return a VBox containing the combined layout of the menu
     */
    private VBox createMainBody(VBox titleBox, VBox controls, Button exitButton) {
        dragArea = new Pane();
        dragArea.setPrefHeight(50);
        dragArea.setStyle("-fx-background-color: transparent;");

        VBox content = new VBox(40, dragArea, titleBox, controls, exitButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(0, 30, 30, 30));
        return content;
    }

    /**
     * Configures the primary stage by embedding the canvas and main content into the scene.
     * Applies necessary styling and positions the stage in the center of the screen.
     *
     * @param canvas  the Canvas used for rendering background animations
     * @param content the VBox containing the main menu content
     */
    private void setupStage(Canvas canvas, VBox content) {
        StackPane root = new StackPane(canvas, content);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        if (!primaryStage.isShowing())
            primaryStage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Creates a custom switch component that toggles between two states.
     * The provided Consumer is triggered when the switch state changes.
     *
     * @param onSwitchChanged a Consumer that is called when the switch state changes
     * @return a StackPane styled as a custom switch component
     */
    private StackPane createCustomSwitch(Consumer<Boolean> onSwitchChanged) {
        StackPane switchContainer = new StackPane();
        switchContainer.setPrefSize(50, 25);
        switchContainer.setStyle("-fx-background-color: #ccc; -fx-background-radius: 15; -fx-cursor: hand;");

        Circle thumb = new Circle(10);
        thumb.setFill(Color.GRAY);
        thumb.setTranslateX(-12);

        TranslateTransition moveThumb = new TranslateTransition(Duration.millis(200), thumb);
        final boolean[] isOn = {false};

        switchContainer.setOnMouseClicked(e -> {
            isOn[0] = !isOn[0];
            if (isOn[0]) {
                switchContainer.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 15; -fx-cursor: hand;");
                thumb.setFill(Color.WHITE);
                moveThumb.setToX(12);
            } else {
                switchContainer.setStyle("-fx-background-color: #ccc; -fx-background-radius: 15; -fx-cursor: hand;");
                thumb.setFill(Color.GRAY);
                moveThumb.setToX(-12);
            }
            moveThumb.play();
            onSwitchChanged.accept(isOn[0]);
        });

        switchContainer.getChildren().add(thumb);
        return switchContainer;
    }

    /**
     * Creates a game mode switcher UI component that provides labeled options and a switcher control.
     *
     * @param labelLeft  the label text for the left side of the switch
     * @param labelRight the label text for the right side of the switch
     * @param switcher   the Node acting as the switcher control
     * @return an HBox containing the labeled game mode switcher
     */
    private HBox createGameModeSwitcher(String labelLeft, String labelRight, Node switcher) {
        Label labelL = new Label(labelLeft);
        labelL.setFont(Font.font(FONT_FAMILY, FontWeight.SEMI_BOLD, 16));
        labelL.setTextFill(Color.WHITE);
        labelL.setPrefWidth(220);
        labelL.setAlignment(Pos.CENTER_RIGHT);

        Label labelR = new Label(labelRight);
        labelR.setFont(Font.font(FONT_FAMILY, FontWeight.SEMI_BOLD, 16));
        labelR.setTextFill(Color.WHITE);
        labelR.setPrefWidth(220);
        labelR.setAlignment(Pos.CENTER_LEFT);

        HBox box = new HBox(10, labelL, switcher, labelR);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(550);
        box.setStyle("-fx-background-color: rgba(68,68,68,0.35); -fx-background-radius: 10; -fx-padding: 10");

        return box;
    }

    /**
     * Stops all running animations, such as the scrolling background.
     * This method should be called when the menu view is no longer needed.
     */
    public void stopAllAnimations() {
        if (background != null) background.stop();
    }

    public void setController(MenuController controller) { this.controller = controller; }

    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }
    public Stage getPrimaryStage() { return primaryStage; }
}
