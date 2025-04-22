package com.game.core.factories;

import com.game.core.entities.Entity;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.config.SceneConfig;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class SpawnerFactory {
    public static Spawner createFromConfig(SceneConfig.SpawnerConfig c, Map<String, Consumer<Entity>> events) {
        Objects.requireNonNull(c);
        Objects.requireNonNull(events);

        if (events.isEmpty())
            throw new NotConfiguredException("Not provided events for spawner");

        if (c.getType() == null)
            throw new InvalidParameterException("Spawner type cannot be null");

        Tile tile = new Tile(c.getTexture(), null);
        Spawner spawner = switch (c.getType()) {
            case PLAYER -> new PlayerSpawner(
                    tile,
                    new Tile(c.getPlayerTexture(), null)
            );
            case BONUS -> new BonusSpawner(tile, c.getCooldown());
        };

        events.forEach(spawner::addEvent);
        return spawner;
    }
}
