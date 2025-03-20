package com.game.core.scene.areas;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.entities.Player;
import com.game.core.managers.CollisionVisitor;

public class KillableArea extends CollidableGameObject implements Area  {
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void applyEffect(Player player) {

    }

    @Override
    public boolean contains(Player player) {
        return false;
    }
}
