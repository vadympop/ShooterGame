package com.game.gui.scenes.menu.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.game.gui.scenes.menu.MenuViewConstants.CARD_SIZE;

/**
 * A GUI component that provides a scrollable list of map selection cards.
 * Each card represents a map, and users can interact with the cards
 * to select a map, triggering a callback.
 */
public class MapSelector {
    private final ScrollPane scrollPane;
    private final HBox mapsContainer;
    private final Consumer<String> onClicked;

    public MapSelector(Consumer<String> onClicked, String[] sceneIds) {
        this.onClicked = onClicked;
        this.mapsContainer = createMapsContainer(sceneIds);
        this.scrollPane = createScrollPane();
    }

    /**
     * Creates the container that holds the map cards.
     *
     * @param sceneIds an array of scene IDs for creating the map cards
     * @return an {@code HBox} containing all the map cards
     */
    private HBox createMapsContainer(String[] sceneIds) {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        for (String sceneId : sceneIds) {
            container.getChildren().add(createMapCard(sceneId));
        }
        return container;
    }

    /**
     * Creates a scrollable container to hold the map cards.
     *
     * @return a {@code ScrollPane} displaying the map cards
     */
    private ScrollPane createScrollPane() {
        ScrollPane pane = new ScrollPane(mapsContainer);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setFitToHeight(true);
        pane.setPannable(true);
        pane.setMaxWidth(CARD_SIZE + 50);
        pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return pane;
    }

    /**
     * Creates an individual map card based on the given scene ID.
     * The card is styled and supports hover and click interactions.
     *
     * @param sceneId the ID of the scene used to configure the card
     * @return a {@code StackPane} representing a single map card
     */
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

        card.setOnMouseClicked(e -> onClicked.accept(sceneId));
        card.setOnMouseEntered(e -> animateScale(card, 1.05));
        card.setOnMouseExited(e -> animateScale(card, 1.0));

        return card;
    }

    /**
     * Animates the scale of a given card to the specified size.
     *
     * @param card    the {@code Node} to scale
     * @param scaleTo the target scale factor
     */
    private void animateScale(Node card, double scaleTo) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), card);
        st.setToX(scaleTo);
        st.setToY(scaleTo);
        st.play();
    }

    /**
     * Animates the scroll pane to navigate through the map cards.
     *
     * @param step the number of steps to scroll. Positive values scroll right,
     *             and negative values scroll left.
     */
    public void scrollByAnimated(int step) {
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double contentWidth = mapsContainer.getWidth();

        if (contentWidth <= viewportWidth) return;

        double scrollStep = (CARD_SIZE + 32) / (contentWidth - viewportWidth);

        double currentHValue = scrollPane.getHvalue();
        double newValue = currentHValue + step * scrollStep;

        newValue = Math.max(scrollPane.getHmin(), Math.min(scrollPane.getHmax(), newValue));

        Timeline scrollPaneTimeline = new Timeline();
        KeyValue kv = new KeyValue(scrollPane.hvalueProperty(), newValue);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        scrollPaneTimeline.getKeyFrames().add(kf);
        scrollPaneTimeline.play();
    }

    public ScrollPane getScrollPane() { return scrollPane; }
}