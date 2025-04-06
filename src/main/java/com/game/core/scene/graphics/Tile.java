package com.game.core.scene.graphics;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.Scaler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.net.URL;


public class Tile {
    private static final String UNDEFINED_TEXTURE = "undefined.png";
    private Image sprite;
    private boolean isTextureUndefined = false;
    private TileType type;
    private float scale;
    private RectangleBounds size;

    public Tile(String sourceTexture, Float scale) {
        this(sourceTexture, TileType.OBJECT, scale);
    }

    public Tile(String sourceTexture, TileType type, Float scale) {
        this(sourceTexture, type, scale, true);
    }

    public Tile(String sourceTexture, TileType type, Float scale, boolean hasDefaultSize) {
        Scaler scaler = Scaler.getInstance();

        setType(type);
        setScale(scale != null ? scale : scaler.getScale());
        loadImage(sourceTexture);

        // RectangleBounds also scaled provided params, so in first if are putted 1 as scale
        // and in the second if width and height should to be scaled already in RectangleBounds
        if (hasDefaultSize || isTextureUndefined()) {
            setSize(new RectangleBounds(scaler.getTileWidth(), scaler.getTileHeight(), 1));
        } else {
            setSize(new RectangleBounds(
                    (float) getSprite().getWidth(),
                    (float) getSprite().getHeight()
            ));
        }
    }

    private void loadImage(String texture) {
        boolean isUndefinedTexture = texture.equals(UNDEFINED_TEXTURE);
        boolean isError = false;

        URL tileURL = getClass().getResource("/textures/" + texture);
        if (tileURL == null) {
            // logging
            isError = true;
        } else {
            try {
                setSprite(new Image(String.valueOf(tileURL.toURI())));
            } catch (URISyntaxException e) {
                // logging
                isError = true;
            }
        }

        if (isError) {
            if (isUndefinedTexture) setTextureIsUndefined(true);
            else loadImage(UNDEFINED_TEXTURE);
        }
    }

    public void draw(GraphicsContext gc, float x, float y) {
        double displayX = x - (getSize().getWidth() / 2);
        double displayY = y - (getSize().getHeight() / 2);
        if (isTextureUndefined()) {
            gc.fillRect(displayX, displayY, getSize().getWidth(), getSize().getHeight());
        } else {
            gc.drawImage(
                    getSprite(),
                    displayX,
                    displayY,
                    getSize().getWidth(),
                    getSize().getHeight()
            );
        }
    }

    public Image getSprite() { return sprite; }
    private void setSprite(Image sprite) { this.sprite = sprite; }

    public TileType getType() { return type; }
    private void setType(TileType type) { this.type = type; }

    public float getScale() { return scale; }
    private void setScale(float scale) { this.scale = scale; }

    public RectangleBounds getSize() { return size; }
    private void setSize(RectangleBounds size) { this.size = size; }

    public boolean isTextureUndefined() { return isTextureUndefined; }
    public void setTextureIsUndefined(boolean isTextureUndefined) { this.isTextureUndefined = isTextureUndefined; }
}
