package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public abstract class Entity extends CollidableGameObject implements Renderable, Updatable {
    private boolean isActive = true;
    private float speed;
    private Tile tile;
    private float rotationAngle = 0f; // 0 angle is right
    private float lastX;
    private float lastY;

    public Entity(Tile tile) {
        setTile(tile);
    }

    protected void move() {
        float angleInRads = (float) Math.toRadians(getRotationAngle());
        float dx = (float) Math.cos(angleInRads);
        float dy = (float) Math.sin(angleInRads);

        setLastX(getX());
        setLastY(getY());

        float newX = getX() + (getSpeed() * dx);
        float newY = getY() + (getSpeed() * dy);
        setPos(newX, newY);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (getState()) render(gc);
    }

    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(getX(), getY());
        gc.rotate(getRotationAngle());
        getTile().draw(gc, 0, 0);
        gc.restore();
    }

    @Override public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }

    public void setState(boolean active) { this.isActive = active; }
    public boolean getState() { return this.isActive; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getRotationAngle() { return this.rotationAngle; }
    protected void setRotationAngle(float rotationAngle) { this.rotationAngle = rotationAngle; }

    public float getLastX() { return lastX; }
    public void setLastX(float lastX) { this.lastX = lastX; }

    public float getLastY() { return lastY; }
    public void setLastY(float lastY) { this.lastY = lastY; }
}
