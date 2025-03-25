package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class BonusSpawner extends GameObject implements Spawner {
    private float cooldown;
    private float timeToNext;
    private Tile tile;

    public BonusSpawner(Tile tile) {
        setTile(tile);
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

    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(float timeToNext) {
        this.timeToNext = timeToNext;
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    private void setTile(Tile tile) {
        this.tile = Objects.requireNonNull(tile);
    }
}
