package com.game.gui.scenes.menu;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MenuView {
    private static final double CARD_SIZE = 180;
    private static final double BACKGROUND_SCROLL_SPEED = 0.2;
    private static final int TILE_SIZE = 64;
    private static final int ARROW_SIZE = 50;
    private static final String FONT_FAMILY = "Arial";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 750;

    private static final String WINDOW_TITLE = "Main menu";
    private static final String[] SCENES = {"001"};

    private static final Image TILE_IMAGE = new Image("/images/menu_bg_tile.png");
    private static final Image CARDS_IMAGE = new Image("/images/cards_bg.png");
    private static final Image LEFT_ARROW_IMAGE = new Image("/images/arrow_left.png");
    private static final Image RIGHT_ARROW_IMAGE = new Image("/images/arrow_right.png");

    private double offsetX = 0;
    private double offsetY = 0;

    private ScrollPane scrollPane;
    private HBox mapsContainer;
    private GraphicsContext gc;
    private Stage primaryStage;
    private MenuController controller;
    private AnimationTimer backgroundAnimation;
    private Timeline scrollPaneTimeline;

    // Constructor to initialize the view
    public MenuView(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        init();
    }

    // Initialize the entire view
    public void init() {
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        VBox titleBox = createTitleBox();
        startBackgroundAnimation();
        mapsContainer = createMapsContainer();
        scrollPane = createScrollPane();
        HBox controls = createArrowButtons();
        Button exitButton = createExitButton();
        VBox content = createMainContent(titleBox, controls, exitButton);

        setupSceneAndStage(canvas, content);
    }

    // Show the stage
    public void show() {
        primaryStage.show();
    }

    // Create the title and subtitle labels
    private VBox createTitleBox() {
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

    // Start the background animation
    private void startBackgroundAnimation() {
        backgroundAnimation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawScrollingBackground(gc);
            }
        };
        backgroundAnimation.start();
    }

    // Create the map container with cards
    private HBox createMapsContainer() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        for (String sceneId : SCENES) {
            container.getChildren().add(createMapCard(sceneId));
        }
        return container;
    }

    // Create a scrollable container for maps
    private ScrollPane createScrollPane() {
        ScrollPane pane = new ScrollPane(mapsContainer);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setFitToHeight(true);
        pane.setPannable(true);
        pane.setMaxWidth(440);
        pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return pane;
    }

    // Create left and right arrow buttons for scrolling
    private HBox createArrowButtons() {
        ImageView leftArrow = createArrowButton(LEFT_ARROW_IMAGE, () -> scrollByAnimated(-1));
        ImageView rightArrow = createArrowButton(RIGHT_ARROW_IMAGE, () -> scrollByAnimated(1));

        BackgroundSize bgSize = new BackgroundSize(
                BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false,
                false,
                true,
                false
        );
        BackgroundImage backgroundImage = new BackgroundImage(
                CARDS_IMAGE,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );

        HBox controls = new HBox(20, leftArrow, scrollPane, rightArrow);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(40));
        controls.setBackground(new Background(backgroundImage));
        return controls;
    }

    // Create an exit button
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

    // Create the main content layout
    private VBox createMainContent(VBox titleBox, HBox controls, Button exitButton) {
        VBox content = new VBox(40, titleBox, controls, exitButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        return content;
    }

    // Setup the scene and primary stage
    private void setupSceneAndStage(Canvas canvas, VBox content) {
        StackPane root = new StackPane(canvas, content);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.BLACK);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
    }

    // Draw the scrolling background
    private void drawScrollingBackground(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.clearRect(0, 0, width, height);

        int horizontalTiles = (int) Math.ceil(width / TILE_SIZE) + 2;
        int verticalTiles = (int) Math.ceil(height / TILE_SIZE) + 2;

        for (int y = 0; y < verticalTiles; y++) {
            for (int x = 0; x < horizontalTiles; x++) {
                gc.drawImage(
                        TILE_IMAGE,
                        x * TILE_SIZE - offsetX,
                        y * TILE_SIZE - offsetY,
                        TILE_SIZE,
                        TILE_SIZE
                );
            }
        }

        offsetX = (offsetX - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        offsetY = (offsetY - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        if (offsetX < 0) offsetX += TILE_SIZE;
        if (offsetY < 0) offsetY += TILE_SIZE;
    }

    private StackPane createMapCard(String sceneId) {
        ImageView imageView = new ImageView(new Image("/images/" + sceneId + "_scene.png"));
        imageView.setFitWidth(CARD_SIZE);
        imageView.setFitHeight(CARD_SIZE);

        Rectangle clip = new Rectangle(CARD_SIZE, CARD_SIZE);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

        StackPane card = new StackPane(imageView);
        card.setPrefSize(CARD_SIZE, CARD_SIZE);
        card.setStyle("-fx-border-radius: 10; -fx-cursor: hand; -fx-border-color: white; -fx-border-width: 5;");

        card.setOnMouseClicked(e -> controller.changeGameScene(sceneId));
        card.setOnMouseEntered(e -> animateScale(card, 1.05));
        card.setOnMouseExited(e -> animateScale(card, 1.0));

        return card;
    }

    // Animate scaling effect for map card
    private void animateScale(Node card, double scaleTo) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
        st.setToX(scaleTo);
        st.setToY(scaleTo);
        st.play();
    }

    // Scroll the map selection horizontally with animation
    private void scrollByAnimated(int step) {
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double contentWidth = mapsContainer.getWidth();

        if (contentWidth <= viewportWidth) return;

        double scrollStep = (CARD_SIZE + 20) / (contentWidth - viewportWidth);

        double currentHValue = scrollPane.getHvalue();
        double newValue = currentHValue + step * scrollStep;

        newValue = Math.max(scrollPane.getHmin(), Math.min(scrollPane.getHmax(), newValue));

        scrollPaneTimeline = new Timeline();
        KeyValue kv = new KeyValue(scrollPane.hvalueProperty(), newValue);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        scrollPaneTimeline.getKeyFrames().add(kf);
        scrollPaneTimeline.play();
    }

    // Create an arrow button for scrolling
    private ImageView createArrowButton(Image image, Runnable onClick) {
        ImageView arrow = new ImageView(image);
        arrow.setFitWidth(ARROW_SIZE);
        arrow.setFitHeight(ARROW_SIZE);
        arrow.setPreserveRatio(true);
        arrow.setCursor(Cursor.HAND);
        arrow.setOpacity(0.8);
        arrow.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onClick.run());
        arrow.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> arrow.setOpacity(1.0));
        arrow.addEventHandler(MouseEvent.MOUSE_EXITED, e -> arrow.setOpacity(0.8));

        return arrow;
    }

    private void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void stopAllAnimations() {
        if (backgroundAnimation != null) backgroundAnimation.stop();
        if (scrollPaneTimeline != null) scrollPaneTimeline.stop();
    }

    public void setController(MenuController controller) { this.controller = controller; }

    public Stage getPrimaryStage() { return primaryStage; }
}
