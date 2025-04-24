package com.game.gui.scenes.menu.components;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static com.game.gui.scenes.menu.MenuViewConstants.BACKGROUND_SCROLL_SPEED;
import static com.game.gui.scenes.menu.MenuViewConstants.TILE_SIZE;

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

    public void start() {
        animationTimer.start();
    }

    public void stop() {
        animationTimer.stop();
    }

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

    private void updateOffsets() {
        offsetX = (offsetX - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        offsetY = (offsetY - BACKGROUND_SCROLL_SPEED) % TILE_SIZE;
        if (offsetX < 0) offsetX += TILE_SIZE;
        if (offsetY < 0) offsetY += TILE_SIZE;
    }
}