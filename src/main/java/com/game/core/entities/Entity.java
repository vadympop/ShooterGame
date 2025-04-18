package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.collisions.CollisionManager;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.DebugUtils;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public abstract class Entity extends CollidableGameObject implements Collidable, Renderable, Updatable {
    private boolean isActive = true;
    private float speed;
    private Tile tile;
    private float rotationAngle = 0f; // 0 angle is right
    private CollisionManager cm;

    public Entity(Tile tile, Bounds hitbox) {
        super(hitbox);
        setTile(tile);
    }

    public float[] getVelocity() {
        float angleInRads = (float) Math.toRadians(getRotationAngle());
        return new float[]{(float) Math.cos(angleInRads), (float) Math.sin(angleInRads)};
    }

    protected void move(double deltaTime) {
        float[] dirs = getVelocity();
        float dx = dirs[0] * (float) deltaTime;
        float dy = dirs[1] * (float) deltaTime;

        float newX = getX() + (getSpeed() * dx);
        float newY = getY() + (getSpeed() * dy);

        boolean[] resetStates = getCm().checkCollisionsFor(this, newX, newY);
        setPos(!resetStates[0] ? newX : getX(), !resetStates[1] ? newY : getY());
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (getState()) render(gc);
        DebugUtils.drawHitboxIfDebug(gc, getHitbox());
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

    @Override
    public String toString() {
        return "Entity{" +
                "isActive=" + isActive +
                ", speed=" + speed +
                ", rotationAngle=" + rotationAngle +
                "}->" + super.toString();
    }
}
