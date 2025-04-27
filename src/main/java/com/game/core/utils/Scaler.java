package com.game.core.utils;

import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.utils.config.SceneConfig;

import java.awt.*;

/**
 * Scaler is a singleton utility class responsible for scaling graphical tiles and scenes based on screen dimensions.
 * It can calculate the scaled size of individual tiles or the entire scene and manages configuration settings.
 */
public class Scaler {
    private static Scaler instance;
    private int horizontalTilesCount;
    private int verticalTilesCount;
    private float defaultTileWidth;
    private float defaultTileHeight;
    private boolean isConfigured = false;

    private Scaler() {}

    public static synchronized Scaler getInstance() {
        if (instance == null) instance = new Scaler();

        return instance;
    }

    /**
     * Calculates the scale factor based on the screen size and tile configuration.
     *
     * @return The calculated scale factor. Returns 1 if not configured.
     */
    public float getScale() {
        if (isNotConfigured()) return 1;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return (float) Math.min(
                screenSize.getWidth() / (getHorizontalTilesCount() * getDefaultTileWidth()),
                screenSize.getHeight() / (getVerticalTilesCount() * getDefaultTileHeight())
        );
    }

    /**
     * Scales a provided value using the calculated scale factor.
     *
     * @param value The value to be scaled.
     * @return The scaled value.
     */
    public float scale(float value) {
        return value * getScale();
    }

    /**
     * Calculates the scaled width of the entire scene.
     *
     * @return The scaled scene width.
     * @throws NotConfiguredException If the Scaler is not configured.
     */
    public float getSceneWidth() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getHorizontalTilesCount() * getDefaultTileWidth());
    }

    /**
     * Calculates the scaled height of the entire scene.
     *
     * @return The scaled scene height.
     * @throws NotConfiguredException If the Scaler is not configured.
     */
    public float getSceneHeight() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getVerticalTilesCount() * getDefaultTileHeight());
    }

    /**
     * Calculates the scaled height of an individual tile.
     *
     * @return The scaled tile height.
     * @throws NotConfiguredException If the Scaler is not configured.
     */
    public float getTileHeight() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getDefaultTileHeight());
    }

    /**
     * Calculates the scaled width of an individual tile.
     *
     * @return The scaled tile width.
     * @throws NotConfiguredException If the Scaler is not configured.
     */
    public float getTileWidth() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getDefaultTileWidth());
    }

    /**
     * Configures the Scaler using a SceneConfig object.
     *
     * @param config The SceneConfig containing tile and scene settings.
     */
    public void setSettings(SceneConfig config) {
        setSettings(
                config.getBackgroundTiles().getFirst().length(),
                config.getBackgroundTiles().size(),
                config.getTileWidth(),
                config.getTileHeight()
        );
    }

    /**
     * Configures the Scaler with the given tile and scene settings.
     *
     * @param horizontalTilesCount The number of horizontal tiles.
     * @param verticalTilesCount   The number of vertical tiles.
     * @param tileWidth            The default width of a tile.
     * @param tileHeight           The default height of a tile.
     * @throws InvalidParameterException If any of the provided parameters are invalid.
     */
    public void setSettings(int horizontalTilesCount, int verticalTilesCount, float tileWidth, float tileHeight) {
        if (horizontalTilesCount <= 0 || verticalTilesCount <= 0 || tileHeight <= 0 || tileWidth <= 0)
            throw new InvalidParameterException("Invalid params was passed to Scaler.setSettings");

        this.horizontalTilesCount = horizontalTilesCount;
        this.verticalTilesCount = verticalTilesCount;
        this.defaultTileWidth = tileWidth;
        this.defaultTileHeight = tileHeight;

        setConfigured(true);
    }

    public int getHorizontalTilesCount() { return horizontalTilesCount; }
    public int getVerticalTilesCount() { return verticalTilesCount; }
    public float getDefaultTileWidth() { return defaultTileWidth; }
    public float getDefaultTileHeight() { return defaultTileHeight; }

    public boolean isNotConfigured() { return !isConfigured; }
    public void setConfigured(boolean configured) { isConfigured = configured; }
}
