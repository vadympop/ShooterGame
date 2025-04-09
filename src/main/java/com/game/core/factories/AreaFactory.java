package com.game.core.factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.scene.areas.Area;
import com.game.core.scene.areas.KillableArea;
import com.game.core.scene.areas.SlowingArea;
import com.game.core.utils.config.SceneConfig;

public class AreaFactory {
    public static Area createFromConfig(SceneConfig.AreaConfig c) {
        Bounds bounds = BoundsFactory.createFromConfig(c.getBounds());
        return switch (c.getType()) {
            case SLOWING -> new SlowingArea(bounds);
            case KILLABLE -> new KillableArea(bounds);
        };
    }
}
