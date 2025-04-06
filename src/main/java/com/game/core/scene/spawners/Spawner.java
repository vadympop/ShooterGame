package com.game.core.scene.spawners;

import com.game.core.behaviour.interfaces.Positionable;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.entities.Entity;

import java.util.function.Consumer;

public interface Spawner extends Updatable, Positionable, Renderable {
    void spawn();
    void addEvent(String name, Consumer<Entity> event);
    Consumer<Entity> getEvent(String name);
}
