package com.game.core.factories;

import com.game.core.entities.Entity;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.config.SceneConfig;

import java.util.function.Consumer;

public class SpawnerFactory {
    public static Spawner createFromConfig(SceneConfig.SpawnerConfig c, Consumer<Entity> onEvent) {
        Tile tile = new Tile(c.getTexture(), null);
        return switch (c.getType()) {
            case PLAYER -> {
                Spawner spawner = new PlayerSpawner(
                        tile,
                        new Tile(c.getPlayerTexture(), null)
                );
                spawner.addEvent("onPlayerCreated", onEvent);

                yield spawner;
            }
            case BONUS -> new BonusSpawner(tile, c.getCooldown());
        };
    }
}
