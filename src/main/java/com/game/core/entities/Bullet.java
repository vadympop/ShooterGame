package com.game.core.entities;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.controllers.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;

public class Bullet extends Entity {
    private final Player owner;
    private final int damage;
    private final float rotationAngle;
    private final float timeToDestroy = 5f;
    private Timer<Bullet> destroyTimer;

    private Bullet(Player owner, Tile tile, int damage, float speed, float rotationAngle, float x, float y) {
        super(tile);

        this.owner = owner;
        this.damage = damage;
        this.rotationAngle = rotationAngle;

        setSpeed(speed);
        setPos(x, y);
        createDestroyTimer();
    }

    public void move() {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void update(double deltaTime) {
        getDestroyTimer().update(deltaTime, this, () -> setDestroyTimer(null));
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

    public Timer<Bullet> getDestroyTimer() {
        return destroyTimer;
    }

    private void setDestroyTimer(Timer<Bullet> timer) {
        destroyTimer = timer;
    }

    private void createDestroyTimer() {
        setDestroyTimer(new Timer<>(getTimeToDestroy(), (x) -> x.setState(false)));
    }

    public static class builder {
        private Player owner;
        private int damage;
        private float speed = 7.0f;
        private float x, y;
        private float rotationAngle;
        private Tile tile;


        public builder owner(Player owner) {
            this.owner = owner;
            this.damage = owner.getBulletDamage();
            this.tile = owner.getBulletTile();
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

        public builder tile(Tile tile) {
            this.tile = tile;
            return this;
        }

        public Bullet build() {
            return new Bullet(owner, tile, damage, speed, rotationAngle, x, y);
        }
    }
}
