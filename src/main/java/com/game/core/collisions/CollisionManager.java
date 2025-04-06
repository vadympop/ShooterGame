package com.game.core.collisions;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.gui.views.game.GameScene;

import java.util.List;

public class CollisionManager {
    private final CollisionVisitor collisionHandler = new CollisionHandler();

    public void checkCollisions(GameScene scene) {
        List<CollidableGameObject> objs = scene.getCollidableGameObjects();

        for (int i = 0; i < objs.size(); i++) {
            CollidableGameObject obj1 = objs.get(i);

            for (int j = i + 1; j < objs.size(); j++) {
                CollidableGameObject obj2 = objs.get(j);

                if(obj1.intersects(obj2)) {
                    objs.get(i).onCollision(collisionHandler, objs.get(j));
                }
            }
        }
    }
}
