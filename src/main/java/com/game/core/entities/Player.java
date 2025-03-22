package com.game.core.entities;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.effects.Effect;
import com.game.core.effects.NoEffect;
import com.game.core.managers.CollisionVisitor;
import com.game.core.strategies.ShootingStrategy;

import java.util.List;

public class Player extends Entity {
    private int maxHealth = 3;
    private int health = maxHealth;

    private int bulletsCount = 5;
    private int bulletDamage = 1;
    private float bulletsReloadDelay = 1;

    private boolean isDead = false;
    private float defaultSpeed = 4f;

    private int rotationDirection = 1;
    private float rotationAngle = 0f;

    private Effect activeEffect;

    private double removeEffectAfter = 0;
    private ShootingStrategy shootingStrategy;

    public Player() {
        setBounds(new CircleBounds(20));
    }

    public void move() {

    }

    public void shoot() {
        setBulletsCount(getBulletsCount() - 1);
        List<Bullet> bullets = getShootingStrategy().shoot(this);
    }

    public void changeRotationDirection() {
        rotationDirection *= -1;
    }

    public boolean applyEffect(Effect effect) {
        if (getActiveEffect() != null || getActiveEffect() instanceof NoEffect) return false;

        setActiveEffect(effect);
        effect.apply(this);
        setRemoveEffectAfter(effect.getDuration());

        return true;
    }

    public void takeDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
        if (this.getHealth() <= 0) {
            setDead(true);
        }
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void update(double deltaTime) {
        if (getRemoveEffectAfter() > 0) {
            setRemoveEffectAfter(getRemoveEffectAfter() - deltaTime);

            if (getRemoveEffectAfter() <= 0) {
                setRemoveEffectAfter(0);
                getActiveEffect().remove(this);
                setActiveEffect(null);
            }
        }
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

    private void setActiveEffect(Effect effect) {
        activeEffect = effect;
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

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public float getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(float defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getRemoveEffectAfter() {
        return removeEffectAfter;
    }

    public void setRemoveEffectAfter(double removeEffectAfter) {
        this.removeEffectAfter = removeEffectAfter;
    }
}
