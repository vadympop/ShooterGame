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
    private float rotationAngle = 0f; // 0 angle is right

    public Entity(Tile tile) {
        setTile(tile);
    }

    protected void move() {
        float rad = (float) Math.toRadians(getRotationAngle());
        float dx = (float) Math.cos(rad);
        float dy = (float) Math.sin(rad);
        float newX = getX() + getSpeed() * dx;
        float newY = getY() + getSpeed() * dy;
        setPos(newX, newY);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (getState()) render(gc);
    }

    public void render(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }

    public void setState(boolean active) { this.isActive = active; }
    public boolean getState() { return this.isActive; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getRotationAngle() { return this.rotationAngle; }
    protected void setRotationAngle(float rotationAngle) { this.rotationAngle = rotationAngle; }
}
