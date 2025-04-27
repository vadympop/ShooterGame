package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.collisions.CollisionManager;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.DebugUtils;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

/**
 * Represents an abstract entity in the game world. This serves as a base class for entities
 * with specific behaviors such as movement, collision handling, and rendering.
 */
public abstract class Entity extends CollidableGameObject implements Collidable, Renderable, Updatable {
    private boolean isActive = true;
    private float speed;
    private Tile tile;
    private float rotationAngle = 0f; // 0 angle is right
    private CollisionManager cm;

    /**
     * Constructs a new Entity with the given visual representation and hitbox bounds.
     *
     * @param tile   the {@link Tile} representing the entity's appearance on the screen.
     * @param hitbox the {@link Bounds} defining the entity's collision box.
     */
    public Entity(Tile tile, Bounds hitbox) {
        super(hitbox);
        setTile(tile);
    }

    /**
     * Calculates and retrieves the current velocity vector of the entity
     * based on its rotation angle.
     *
     * @return an array containing the velocity components [x, y].
     */
    public float[] getVelocity() {
        float angleInRads = (float) Math.toRadians(getRotationAngle());
        return new float[]{(float) Math.cos(angleInRads), (float) Math.sin(angleInRads)};
    }

    /**
     * Moves the entity in the direction of its velocity, handling collisions and boundary constraints.
     *
     * @param deltaTime the time elapsed since the previous frame.
     */
    protected void move(double deltaTime) {
        if (getCm() == null) throw new NotConfiguredException("Collision manager dont specified to " + this);

        float[] dirs = getVelocity();
        float dx = dirs[0] * (float) deltaTime;
        float dy = dirs[1] * (float) deltaTime;

        float newX = getX() + (getSpeed() * dx);
        float newY = getY() + (getSpeed() * dy);

        boolean[] resetStates = getCm().checkCollisionsFor(this, newX, newY);
        setPos(!resetStates[0] ? newX : getX(), !resetStates[1] ? newY : getY());

        getCm().applyAreas(this);
    }

    /**
     * Draws the entity on the specified graphics context. If debugging is enabled,
     * the entity's hitbox will also be rendered.
     *
     * @param gc the {@link GraphicsContext} to render the entity on.
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (getState()) render(gc);
        DebugUtils.drawHitboxIfDebug(gc, getHitbox());
    }

    /**
     * Renders the visual appearance of the entity on the specified graphics context.
     *
     * @param gc the {@link GraphicsContext} to render the entity on.
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(getX(), getY());
        gc.rotate(getRotationAngle());
        getTile().draw(gc, 0, 0);
        gc.restore();
    }

    @Override public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }

    /**
     * Sets the active state of the entity, determining if it should be updated or rendered.
     *
     * @param active {@code true} to activate the entity, {@code false} to deactivate it.
     */
    public void setState(boolean active) {
        this.isActive = active;
    }

    /**
     * Checks whether the entity is active.
     *
     * @return {@code true} if the entity is active, otherwise {@code false}.
     */
    public boolean getState() {
        return this.isActive;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getRotationAngle() { return this.rotationAngle; }

    /**
     * Sets the rotation angle of the entity, constrained within a 0-360 degree range.
     *
     * @param rotationAngle the new rotation angle to set.
     */
    protected void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle % 360;
    }

    /**
     * Retrieves the collision manager assigned to the entity.
     *
     * @return the {@link CollisionManager} handling the entity's collisions.
     */
    public CollisionManager getCm() {
        return cm;
    }

    /**
     * Sets the collision manager responsible for handling the entity's collision logic.
     *
     * @param cm the {@link CollisionManager} to assign.
     * @throws NullPointerException if the specified collision manager is {@code null}.
     */
    public void setCm(CollisionManager cm) {
        this.cm = Objects.requireNonNull(cm);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "isActive=" + isActive +
                ", speed=" + speed +
                ", rotationAngle=" + rotationAngle +
                "}->" + super.toString();
    }
}
