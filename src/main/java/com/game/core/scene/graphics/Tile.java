package com.game.core.scene.graphics;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.PositionUtils;
import com.game.core.utils.ResourceUtils;
import com.game.core.utils.Scaler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;


/**
 * Represents a tile in the game with graphical properties such as texture, size, scale, and type.
 * Handles loading textures, scaling them, and drawing the tile on a canvas.
 */
public class Tile {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tile.class);
    private static final String UNDEFINED_TEXTURE = "undefined.png";

    private Image sprite;
    private boolean isTextureUndefined = false;
    private TileType type;
    private float scale;
    private RectangleBounds size;

    /**
     * Constructs a `Tile` object using the provided texture and scale.
     *
     * @param sourceTexture the file name of the texture to use for this tile.
     * @param scale         the scaling factor for the tile.
     */
    public Tile(String sourceTexture, Float scale) {
        this(sourceTexture, TileType.OBJECT, scale);
    }

    /**
     * Constructs a `Tile` object with a specified texture, type, and scale.
     *
     * @param sourceTexture the file name of the texture to use for this tile.
     * @param type          the type of the tile (e.g., OBJECT, BACKGROUND).
     * @param scale         the scaling factor for the tile.
     */
    public Tile(String sourceTexture, TileType type, Float scale) {
        this(sourceTexture, type, scale, true);
    }

    /**
     * Constructs a `Tile` object with detailed configuration for texture, type, scale, and size.
     *
     * @param sourceTexture  the file name of the texture to use for this tile.
     * @param type           the type of the tile (e.g., OBJECT, BACKGROUND).
     * @param scale          the scaling factor for the tile.
     * @param hasDefaultSize specifies whether the default size should be used or not.
     */
    public Tile(String sourceTexture, TileType type, Float scale, boolean hasDefaultSize) {
        Scaler scaler = Scaler.getInstance();

        setType(type);
        setScale(scale != null ? scale : scaler.getScale());
        loadImage(sourceTexture);

        // RectangleBounds also scaled provided params, so in first if are putted 1 as scale
        // and in the second if width and height should to be scaled already in RectangleBounds
        if (hasDefaultSize || isTextureUndefined()) {
            LOGGER.debug("Process scaling in Bounds -> pass scale argument as '1'");
            // because scaler.getTileWidth/getTileHeight already scaled
            setSize(new RectangleBounds(scaler.getTileWidth(), scaler.getTileHeight(), 1));
        } else {
            LOGGER.debug("Use custom sizes, so this sizes need to be scaled -> use constructor without scale arg");
            setSize(new RectangleBounds(
                    (float) getSprite().getWidth(),
                    (float) getSprite().getHeight()
            ));
        }
    }

    /**
     * Loads the specified texture into the tile or assigns a default texture if the specified resource is unavailable.
     *
     * @param texture the file name of the texture to load.
     */
    private void loadImage(String texture) {
        boolean isUndefinedTexture = texture.equals(UNDEFINED_TEXTURE);
        boolean isError = false;

        URL tileURL = ResourceUtils.getResource("/textures/" + texture);
        if (tileURL == null) {
            LOGGER.warn("Tile texture={} not found", texture);
            isError = true;
        } else {
            try {
                setSprite(new Image(String.valueOf(tileURL.toURI())));
            } catch (URISyntaxException e) {
                LOGGER.warn("Tile texture={} url is bad", tileURL);
                isError = true;
            } catch (RuntimeException e) {
                // Main case for this exception is an exception when running tests and javafx not started
                setTextureIsUndefined(true);
                return;
            }
        }

        if (isError) {
            if (isUndefinedTexture) {
                LOGGER.warn("Dont find any texture");
                setTextureIsUndefined(true);
            }
            else loadImage(UNDEFINED_TEXTURE);
        }
    }

    /**
     * Renders the tile on the given canvas at the specified coordinates.
     * If the texture is undefined, it draws a placeholder rectangle.
     *
     * @param gc the `GraphicsContext` used for rendering the tile.
     * @param x  the x-coordinate of the tile's position.
     * @param y  the y-coordinate of the tile's position.
     */
    public void draw(GraphicsContext gc, float x, float y) {
        double[] displayPos = PositionUtils.generateDisplayPos(x, y, getSize());
        if (isTextureUndefined()) {
            gc.fillRect(displayPos[0], displayPos[1], getSize().getWidth(), getSize().getHeight());
        } else {
            gc.drawImage(
                    getSprite(),
                    displayPos[0],
                    displayPos[1],
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
