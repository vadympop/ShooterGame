package com.game.core.collisions;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollisionManager.class);

    private final CollisionVisitor collisionHandler = new CollisionHandler();
    private final List<Collidable> collidableObjects = new ArrayList<>();

    public void removeEntities(List<Entity> toRemove) {
        collidableObjects.removeAll(toRemove);
    }

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

    private Bounds createTempBounds(Bounds original, float newX, float newY) {
        Bounds bounds = original.copy();
        bounds.setPos(newX, newY);

        return bounds;
    }

    public void addObject(Collidable obj) { this.collidableObjects.add(obj); }
}
