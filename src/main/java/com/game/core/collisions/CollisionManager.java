package com.game.core.collisions;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.scene.areas.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages collision detection and area effects for all entities and collidable objects in the game.
 * This class is responsible for checking entity collisions, applying area effects, and managing
 * collidable objects and areas.
 */
public class CollisionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollisionManager.class);

    private final CollisionVisitor collisionHandler = new CollisionHandler();
    private final List<Collidable> collidableObjects = new ArrayList<>();
    private final List<Area> areas = new ArrayList<>();

    /**
     * Removes entities from the list of collidable objects.
     *
     * @param toRemove a list of entities to be removed from the collision list
     */
    public void removeEntities(List<Entity> toRemove) {
        collidableObjects.removeAll(toRemove);
    }

    /**
     * Checks for collisions between the given entity and other collidable objects.
     * Determines if the movement along the X or Y axis results in a collision and invokes
     * collision handling logic if needed.
     *
     * @param entity the entity whose collisions need to be checked
     * @param newX   the proposed new X-coordinate for the entity
     * @param newY   the proposed new Y-coordinate for the entity
     * @return a boolean array where [0] indicates if movement along the X-axis is blocked
     * and [1] indicates if movement along the Y-axis is blocked
     */
    public boolean[] checkCollisionsFor(Collidable entity, float newX, float newY) {
        Bounds originalHitbox = entity.getHitbox();
        Bounds newHitboxX = createTempBounds(originalHitbox, newX, originalHitbox.getY());
        Bounds newHitboxY = createTempBounds(originalHitbox, originalHitbox.getX(), newY);
        boolean[] resetStates = {false, false};

        for (Collidable other : collidableObjects) {
            boolean xIntersects = newHitboxX.intersects(other.getHitbox());
            boolean yIntersects = newHitboxY.intersects(other.getHitbox());
            if (other != entity && (xIntersects || yIntersects)) {
                if (xIntersects) resetStates[0] = true;
                if (yIntersects) resetStates[1] = true;

                LOGGER.debug(
                        "{} collide with {}, reset states are x={}, y={}",
                        entity.getClass().getSimpleName(),
                        other.getClass().getSimpleName(),
                        resetStates[0],
                        resetStates[1]
                );
                entity.onCollision(collisionHandler, other);
            }
        }

        return resetStates;
    }

    /**
     * Applies the effects of any active areas to the given entity.
     * Specifically checks if a Player entity is within an area's bounds
     * and applies effects accordingly.
     *
     * @param entity the entity to which area effects should be applied
     */
    public void applyAreas(Entity entity) {
        if (entity instanceof Player player)
            areas.stream()
                    .filter(area -> area.contains(player))
                    .forEach(area -> area.applyEffect(player));
    }

    /**
     * Creates a temporary bounds object for collision checks based on a new position.
     *
     * @param original the original bounds of the entity
     * @param newX     the new X-coordinate for the bounds
     * @param newY     the new Y-coordinate for the bounds
     * @return a new instance of Bounds with the updated position
     */
    private Bounds createTempBounds(Bounds original, float newX, float newY) {
        Bounds bounds = original.copy();
        bounds.setPos(newX, newY);

        return bounds;
    }

    /**
     * Adds a collidable object to the manager for collision detection.
     *
     * @param obj the collidable object to be added
     */
    public void addObject(Collidable obj) {
        this.collidableObjects.add(obj);
    }

    /**
     * Adds an area to the manager for applying effects to entities.
     *
     * @param area the area to be added
     */
    public void addArea(Area area) {
        this.areas.add(area);
    }
}
