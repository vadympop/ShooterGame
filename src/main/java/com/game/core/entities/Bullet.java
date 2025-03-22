package com.game.core.entities;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.managers.CollisionVisitor;

public class Bullet extends Entity {
    private final Player owner;
    private final int damage;
    private final float rotationAngle;
    private float timeToDestroy;

    private Bullet(Player owner, int damage, float speed, float rotationAngle, float x, float y) {
        this.owner = owner;
        this.damage = damage;
        this.setSpeed(speed);
        this.rotationAngle = rotationAngle;
        this.setPos(x, y);
    }

    public void move() {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void update(double deltaTime) {

    }

    public Player getOwner() {
        return this.owner;
    }

    public int getDamage() {
        return this.damage;
    }

    public float getTimeToDestroy() {
        return this.timeToDestroy;
    }

    public float getRotationAngle() {
        return this.rotationAngle;
    }

    public static class builder {
        private Player owner;
        private int damage;
        private float speed = 7.0f;
        private float x;
        private float y;
        private float rotationAngle;


        public builder owner(Player owner) {
            this.owner = owner;
            this.damage = owner.getBulletDamage();
            return this;
        }

        public builder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public builder pos(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public builder rotationAngle(float rotationAngle) {
            this.rotationAngle = rotationAngle;
            return this;
        }

        public Bullet build() {
            return new Bullet(owner, damage, speed, rotationAngle, x, y);
        }
    }
}
