package com.game.core.collisions;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private final CollisionVisitor collisionHandler = new CollisionHandler();
    private final List<Collidable> collidableObjects = new ArrayList<>();

    public void checkCollisionsFor(Collidable entity, float newX, float newY) {
        Bounds originalHitbox = entity.getHitbox();
        Bounds newHitbox = createTempBounds(originalHitbox, newX, newY);

        for (Collidable other : collidableObjects) {
            if (other != entity && newHitbox.intersects(other.getHitbox())) {
                System.out.println("COLLISION");
                entity.onCollision(collisionHandler, other);
            }
        }
    }

    private Bounds createTempBounds(Bounds original, float newX, float newY) {
        Bounds bounds = original.copy();
        bounds.setPos(newX, newY);

        return bounds;
    }

    public void addObject(Collidable obj) {
        this.collidableObjects.add(obj);
    }
}
