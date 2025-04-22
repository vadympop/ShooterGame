package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.entities.Entity;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.factories.BonusFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class BonusSpawner extends GameObject implements Spawner {
    private final float cooldown;
    private final Map<String, Consumer<Entity>> events = new HashMap<>();
    private Timer<BonusSpawner> spawnTimer;
    private final Tile tile;
    private Bonus currentBonus;

    public BonusSpawner(Tile tile, float cooldown) {
        this.tile = Objects.requireNonNull(tile);
        this.cooldown = cooldown;

        createSpawnTimer();
    }

    @Override
    public void spawn() {
        if (currentBonus != null && currentBonus.getState()) return;

        Bonus bonus = BonusFactory.create(BonusType.randomBonus());
        bonus.setPos(getX(), getY());
        currentBonus = bonus;

        Consumer<Entity> event = getEvent("onEntityCreated");
        if (event != null) event.accept(bonus);
    }

    @Override
    public void addEvent(String name, Consumer<Entity> event) {
        this.events.put(name, event);
    }

    @Override
    public Consumer<Entity> getEvent(String name) {
        return this.events.get(name);
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public void update(double deltaTime) {
        getSpawnTimer().update(deltaTime, this, null);
    }

    public Timer<BonusSpawner> getSpawnTimer() {
        return this.spawnTimer;
    }

    private void createSpawnTimer() {
        this.spawnTimer = new Timer<>(getCooldown(), BonusSpawner::spawn, true);
    }

    public float getCooldown() { return cooldown; }

    @Override public Tile getTile() { return tile; }
}
