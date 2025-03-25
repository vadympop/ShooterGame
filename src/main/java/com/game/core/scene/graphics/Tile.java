package com.game.core.scene.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Tile {
    private Image sprite;
    private TileType type;

    public Tile(String sourceTexture) {
        this(sourceTexture, TileType.OBJECT);
    }

    public Tile(String sourceTexture, TileType type) {
        setSprite(new Image(String.valueOf(getClass().getResource(sourceTexture))));
        setType(type);
    }

    public void draw(GraphicsContext gc, float x, float y) {
        double displayX = x - (getSprite().getWidth() / 2);
        double displayY = y - (getSprite().getHeight() / 2);
        gc.drawImage(getSprite(), displayX, displayY);
    }

    public Image getSprite() {
        return sprite;
    }

    private void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public TileType getType() {
        return type;
    }

    private void setType(TileType type) {
        this.type = type;
    }
}
