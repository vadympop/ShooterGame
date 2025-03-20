package com.game.core.scene.blocks;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.managers.CollisionVisitor;

public class SolidBlock extends CollidableGameObject implements Block {
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void render() {

    }
}
