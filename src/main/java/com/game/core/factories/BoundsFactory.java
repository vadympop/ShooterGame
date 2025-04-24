package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;

public class BoundsFactory {
    public static Bounds createFromConfig(SceneConfig.BoundsConfig c) {
        return switch (c.getType()) {
            case CIRCLE -> new CircleBounds(c.getRadius());
            case RECTANGLE -> new RectangleBounds(c.getWidth(), c.getHeight());
        };
    }

    public static RectangleBounds createForBlock(SceneConfig.BoundsConfig c) {
        SceneConfig config = ConfigManager.getInstance().getConfig();
        float width = c != null ? c.getWidth() : config.getTileWidth();
        float height = c != null ? c.getHeight() : config.getTileHeight();
        return new RectangleBounds(width, height);
    }
}
