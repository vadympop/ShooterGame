package com.game.gui.scenes.menu.components;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static com.game.gui.scenes.menu.MenuViewConstants.BACKGROUND_SCROLL_SPEED;
import static com.game.gui.scenes.menu.MenuViewConstants.TILE_SIZE;

/**
 * A class responsible for creating a scrolling background effect using a tile image.
 * The background is rendered onto a `GraphicsContext` and animates by continuously updating offsets.
 */
public class ScrollingBackground {
    private final GraphicsContext gc;
    private final Image tileImage;
    private final double width;
    private final double height;
    private final AnimationTimer animationTimer;
    private double offsetX;
    private double offsetY;

    public ScrollingBackground(GraphicsContext gc, Image tileImage, double width, double height) {
        this.gc = gc;
        this.width = width;
        this.height = height;
        this.offsetX = 0;
        this.offsetY = 0;
        this.tileImage = tileImage;
        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
                updateOffsets();
            }
        };
    }

    /**
     * Starts the animation timer to begin rendering and scrolling the background.
     */
    public void start() {
        animationTimer.start();
    }

    /**
     * Stops the animation timer and halts rendering and scrolling of the background.
     */
    public void stop() {
        animationTimer.stop();
    }

    /**
     * Renders the scrolling background by drawing repeated tiles onto the `GraphicsContext`.
     * It ensures sufficient tiles are rendered to cover the visible area and apply the offset.
     */
    private void draw() {
        gc.clearRect(0, 0, width, height);

        int horizontalTiles = (int) Math.ceil(width / TILE_SIZE) + 2;
        int verticalTiles = (int) Math.ceil(height / TILE_SIZE) + 2;

        for (int y = 0; y < verticalTiles; y++) {
            for (int x = 0; x < horizontalTiles; x++) {
                gc.drawImage(
                        tileImage,
                        x * TILE_SIZE - offsetX,
                        y * TILE_SIZE - offsetY,
                        TILE_SIZE,
                        TILE_SIZE
                );
            }
        }
    }

    /**
     * Updates the x and y offsets used for scrolling the background.
     * The offsets loop around when they exceed the tile size.
     */
    private void updateOffsets() {
        offsetX = (offsetX - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        offsetY = (offsetY - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        if (offsetX < 0) offsetX += TILE_SIZE;
        if (offsetY < 0) offsetY += TILE_SIZE;
    }
}