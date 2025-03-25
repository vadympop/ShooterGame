package com.game.core.scene.spawners;

import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;

public interface Spawner extends Updatable, Renderable {
    void spawn();
}
