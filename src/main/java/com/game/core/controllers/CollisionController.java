package com.game.core.controllers;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.scene.Scene;

import java.util.List;

public class CollisionController {
    private final CollisionVisitor collisionHandler = new CollisionHandler();

    public void checkCollisions(Scene scene) {
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
