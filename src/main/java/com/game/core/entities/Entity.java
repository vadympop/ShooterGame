package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public abstract class Entity extends CollidableGameObject implements Renderable, Updatable {
    private boolean isActive;
    private float speed;
    private Tile tile;

    public Entity(Tile tile) {
        setTile(tile);
    }

    public void render(GraphicsContext gc) {
        if (getState()) draw(gc);
    }

    public void setState(boolean active) {
        this.isActive = active;
    }

    public boolean getState() {
        return this.isActive;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    private void setTile(Tile tile) {
        this.tile = Objects.requireNonNull(tile);
    }
}
