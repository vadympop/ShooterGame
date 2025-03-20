package com.game.core.scene.spawners;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.managers.CollisionVisitor;

public class BonusSpawner extends CollidableGameObject implements Spawner {
    private float cooldown;
    private float timeToNext;

    @Override
    public void spawn() {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getTimeToNext() {
        return timeToNext;
    }

    public void setTimeToNext(float timeToNext) {
        this.timeToNext = timeToNext;
    }
}
