package com.game.core.scene.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Tile {
    private Image sprite;
    private TileType type;


    public void draw(GraphicsContext gc, float x, float y) {
        double displayX = getSprite().getWidth();
        gc.drawImage(getSprite(), x, y);
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
