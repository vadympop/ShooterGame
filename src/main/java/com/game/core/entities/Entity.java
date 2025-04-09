package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.collisions.CollisionManager;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public abstract class Entity extends CollidableGameObject implements Renderable, Updatable {
    private boolean isActive = true;
    private float speed;
    private Tile tile;
    private float rotationAngle = 0f; // 0 angle is right
    private CollisionManager cm;

    public Entity(Tile tile, Bounds hitbox) {
        super(hitbox);
        setTile(tile);
    }

    protected void move() {
        float angleInRads = (float) Math.toRadians(getRotationAngle());
        float dx = (float) Math.cos(angleInRads);
        float dy = (float) Math.sin(angleInRads);

        float newX = getX() + (getSpeed() * dx);
        float newY = getY() + (getSpeed() * dy);

        boolean[] resetStates = getCm().checkCollisionsFor(this, newX, newY);
        if (resetStates[0] || resetStates[1]) System.out.println("dx: " + dx + " | " + "dy: " + dy + " - reset x:" + resetStates[0] + ", y: " + resetStates[1]);
        setPos(!resetStates[0] ? newX : getX(), !resetStates[1] ? newY : getY());
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

    // Setter and getter for collision manager
    public CollisionManager getCm() { return cm; }
    public void setCm(CollisionManager cm) { this.cm = cm; }
}
