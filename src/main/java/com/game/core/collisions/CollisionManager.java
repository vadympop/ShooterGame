package com.game.core.collisions;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
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
        if (original instanceof CircleBounds circle) {
            CircleBounds temp = new CircleBounds(circle.getRadius());
            temp.setPos(newX, newY);
            return temp;
        } else if (original instanceof RectangleBounds rect) {
            RectangleBounds temp = new RectangleBounds(rect.getWidth(), rect.getHeight());
            temp.setPos(newX, newY);
            return temp;
        }
        throw new IllegalArgumentException("Unknown Bounds type");
    }

    public void addObject(Collidable obj) {
        this.collidableObjects.add(obj);
    }
}
