package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.scene.areas.Area;
import com.game.core.scene.areas.KillableArea;
import com.game.core.scene.areas.SlowingArea;
import com.game.core.utils.config.SceneConfig;

/**
 * Factory class responsible for creating {@link Area} objects based on configuration data.
 * This class provides an abstraction for instantiating specific {@link Area} types
 * such as {@link SlowingArea} and {@link KillableArea} using {@link SceneConfig.AreaConfig}.
 */
public class AreaFactory {
    /**
     * Creates an {@link Area} instance based on the provided {@link SceneConfig.AreaConfig}.
     *
     * @param c the configuration data used to determine the type of {@link Area} and its properties
     * @return a newly created instance of {@link Area}, such as {@link SlowingArea} or {@link KillableArea}
     * based on the configuration
     */
    public static Area createFromConfig(SceneConfig.AreaConfig c) {
        Bounds bounds = BoundsFactory.createFromConfig(c.getBounds());
        return switch (c.getType()) {
            case SLOWING -> new SlowingArea(bounds);
            case KILLABLE -> new KillableArea(bounds);
        };
    }
}
