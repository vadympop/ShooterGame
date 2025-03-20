package com.game.core.scene.spawners;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.managers.CollisionVisitor;

public class PlayerSpawner extends CollidableGameObject implements Spawner {
    @Override
    public void spawn() {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }
}
