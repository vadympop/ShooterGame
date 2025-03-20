package com.game.core.entities;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.managers.CollisionVisitor;

public class Bullet extends Entity {
    private Player owner;
    private int damage;
    private float speed;
    private float timeToDestroy;

    public void move() {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void update() {

    }

    public static class builder {
        private Player owner;
        private int damage;
        private float speed;

    }
}
