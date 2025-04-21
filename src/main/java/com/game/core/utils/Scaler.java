package com.game.core.utils;

import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.utils.config.SceneConfig;

import java.awt.*;

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

    public float getScale() {
        if (isNotConfigured()) return 1;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return (float) Math.min(
                screenSize.getWidth() / (getHorizontalTilesCount() * getDefaultTileWidth()),
                screenSize.getHeight() / (getVerticalTilesCount() * getDefaultTileHeight())
        );
    }

    public float scale(float value) {
        return value * getScale();
    }

    public float getSceneWidth() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getHorizontalTilesCount() * getDefaultTileWidth());
    }

    public float getSceneHeight() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getVerticalTilesCount() * getDefaultTileHeight());
    }

    public float getTileHeight() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getDefaultTileHeight());
    }

    public float getTileWidth() {
        if (isNotConfigured()) throw new NotConfiguredException("Scaler is not configured");

        return scale(getDefaultTileWidth());
    }

    public void setSettings(SceneConfig config) {
        setSettings(
                config.getBackgroundTiles().getFirst().length(),
                config.getBackgroundTiles().size(),
                config.getTileWidth(),
                config.getTileHeight()
        );
    }

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
