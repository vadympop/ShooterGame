package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;

/**
 * A factory class for creating different types of {@link Bounds} objects
 * based on configuration data provided in {@link SceneConfig.BoundsConfig}.
 */
public class BoundsFactory {
    /**
     * Creates a {@link Bounds} object based on the configuration provided.
     *
     * @param c the {@link SceneConfig.BoundsConfig} containing the bounds configuration.
     *          Must not be null.
     * @return a new {@link CircleBounds} or {@link RectangleBounds} based on the type specified in the configuration.
     */
    public static Bounds createFromConfig(SceneConfig.BoundsConfig c) {
        return switch (c.getType()) {
            case CIRCLE -> new CircleBounds(c.getRadius());
            case RECTANGLE -> new RectangleBounds(c.getWidth(), c.getHeight());
        };
    }

    /**
     * Creates a {@link RectangleBounds} object based on the provided block configuration.
     * If no configuration is provided, it uses default tile dimensions from the scene configuration.
     *
     * @param c the {@link SceneConfig.BoundsConfig} for the specific block, or null to use default dimensions.
     * @return a new {@link RectangleBounds} object.
     */
    public static RectangleBounds createForBlock(SceneConfig.BoundsConfig c) {
        SceneConfig config = ConfigManager.getInstance().getConfig();
        float width = c != null ? c.getWidth() : config.getTileWidth();
        float height = c != null ? c.getHeight() : config.getTileHeight();
        return new RectangleBounds(width, height);
    }
}
