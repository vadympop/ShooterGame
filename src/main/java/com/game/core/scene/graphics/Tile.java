package com.game.core.scene.graphics;

import com.game.core.behaviour.bounds.RectangleBounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;


public class Tile {
    private Image sprite;
    private TileType type;
    private float scale;
    private RectangleBounds size;

    public Tile(String sourceTexture, float scale) {
        this(sourceTexture, TileType.OBJECT, scale);
    }

    public Tile(String sourceTexture, TileType type, float scale) {
        this(sourceTexture, type, scale, null);
    }

    public Tile(String sourceTexture, TileType type, float scale, RectangleBounds size) {
        URL tileURL = getClass().getResource("/textures/" + sourceTexture);
        try {
            setSprite(new Image(String.valueOf(tileURL.toURI())));
        } catch (URISyntaxException | NullPointerException e) {
            System.out.println(sourceTexture);
        }

        setType(type);
        setScale(scale);

        setSize(Objects.requireNonNullElseGet(
                size,
                () -> new RectangleBounds(
                        (float) getSprite().getWidth(),
                        (float) getSprite().getHeight()
                )
        ));
    }

    public void draw(GraphicsContext gc, float x, float y) {
        double displayX = x - (getSprite().getWidth() / 2);
        double displayY = y - (getSprite().getHeight() / 2);
        gc.drawImage(
                getSprite(),
                displayX,
                displayY,
                getSize().getWidth() * getScale(),
                getSize().getHeight() * getScale()
        );
    }

    public Image getSprite() { return sprite; }
    private void setSprite(Image sprite) { this.sprite = sprite; }

    public TileType getType() { return type; }
    private void setType(TileType type) { this.type = type; }

    public float getScale() { return scale; }
    private void setScale(float scale) { this.scale = scale; }

    public RectangleBounds getSize() { return size; }
    private void setSize(RectangleBounds size) { this.size = size; }
}
