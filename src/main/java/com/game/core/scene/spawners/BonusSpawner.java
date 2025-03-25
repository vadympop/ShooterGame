package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class BonusSpawner extends GameObject implements Spawner {
    private float cooldown;
    private Timer<BonusSpawner> spawnTimer;
    private Tile tile;

    public BonusSpawner(Tile tile, float cooldown) {
        setTile(tile);
        setCooldown(cooldown);
        createSpawnTimer();
    }

    @Override
    public void spawn() {

    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public void update(double deltaTime) {
        getSpawnTimer().decreaseTime(deltaTime);

        if (getSpawnTimer().getTimeLeft() <= 0) {
            getSpawnTimer().getFunc().accept(this);
            getSpawnTimer().setTimeLeft(getSpawnTimer().getBaseTime());
        }
    }

    public Timer<BonusSpawner> getSpawnTimer() {
        return this.spawnTimer;
    }

    private void createSpawnTimer() {
        this.spawnTimer = new Timer<>(getCooldown(), BonusSpawner::spawn, true);
    }

    public float getCooldown() {
        return cooldown;
    }

    private void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    private void setTile(Tile tile) {
        this.tile = Objects.requireNonNull(tile);
    }
}
