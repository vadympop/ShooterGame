package com.game.core.entities;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.managers.CollisionVisitor;
import com.game.core.strategies.ShootingStrategy;

public class Player extends Entity {
    private int health;
    private Effect activeEffect;
    private int bulletsCount;
    private boolean isDead;
    private int rotationDirection;
    private float bulletsReloadDelay;
    private int deathCount;
    private float rotationAngle;
    private ShootingStrategy shootingStrategy;

    public void move() {

    }

    public void shoot() {

    }

    public void changeRotationDirection() {

    }

    public void applyEffect(Effect effect) {

    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {

    }

    @Override
    public void update() {

    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Effect getActiveEffect() {
        return this.activeEffect;
    }

    public int getBulletsCount() {
        return this.bulletsCount;
    }

    public void setBulletsCount(int bulletsCount) {
        this.bulletsCount = bulletsCount;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public int getRotationDirection() {
        return this.rotationDirection;
    }

    public float getBulletsReloadDelay() {
        return this.bulletsReloadDelay;
    }

    public void setBulletsReloadDelay(float bulletsReloadDelay) {
        this.bulletsReloadDelay = bulletsReloadDelay;
    }

    public int getDeathCount() {
        return this.deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public float getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public ShootingStrategy getShootingStrategy() {
        return this.shootingStrategy;
    }

    public void setShootingStrategy(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }
}
