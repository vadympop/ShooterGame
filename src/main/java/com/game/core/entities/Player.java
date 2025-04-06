package com.game.core.entities;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.effects.NoEffect;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.strategies.ShootingStrategy;
import com.game.core.strategies.SingleShootStrategy;
import com.game.core.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Player extends Entity {
    private PlayerSpawner spawner;
    private int maxHealth;
    private int health;

    private int maxBulletsCount;
    private int bulletsCount;
    private int bulletDamage;
    private float bulletsReloadDelay;
    private Tile bulletTile;

    private boolean isMoving = false;
    private boolean isDead = false;
    private float defaultSpeed;

    private float rotationSpeed;
    private int rotationDirection = 1;

    private final List<Timer<Player>> timers = new ArrayList<>();
    private Effect activeEffect;
    private boolean hasShield = false;
    private ShootingStrategy shootingStrategy = new SingleShootStrategy();

    public Player(
            PlayerSpawner spawner,
            Bounds hitbox,
            Tile tile,
            Tile bulletTile,
            int maxHealth,
            int maxBulletsCount,
            int bulletDamage,
            float bulletsReloadDelay,
            float defaultSpeed,
            float rotationSpeed
    ) {
        super(tile, hitbox);

        setMaxHealth(maxHealth);
        setHealth(getMaxHealth());
        setMaxBulletsCount(maxBulletsCount);
        setBulletsCount(getMaxBulletsCount());
        setBulletDamage(bulletDamage);
        setBulletsReloadDelay(bulletsReloadDelay);
        setDefaultSpeed(defaultSpeed);
        setSpeed(defaultSpeed);
        setRotationSpeed(rotationSpeed);
        setSpawner(spawner);
        setBulletTile(bulletTile);
        timers.add(new Timer<>(getBulletsReloadDelay(), (x) -> {
            if (x.getBulletsCount() == x.getMaxBulletsCount()) return;

            x.setBulletsCount(x.getBulletsCount() + 1);
        }, true));
    }

    public void shoot() {
        setBulletsCount(getBulletsCount() - 1);
        List<Bullet> bullets = getShootingStrategy().shoot(this);
    }

    public boolean applyEffect(Effect effect) {
        if (getActiveEffect() != null || getActiveEffect() instanceof NoEffect) return false;

        setActiveEffect(effect);
        effect.apply(this);

        timers.add(new Timer<>(effect.getDuration(), (x) -> {
            x.getActiveEffect().remove(x);
            x.setActiveEffect(null);
        }));

        return true;
    }

    public void takeDamage(int damage) {
        this.setHealth(this.getHealth() - damage);
        if (this.getHealth() <= 0) {
            setDead(true);
            timers.add(new Timer<>(5f, (p) -> p.getSpawner().spawn()));
        }
    }

    public void respawn(float x, float y) {
        setPos(x, y);
        setHealth(getMaxHealth());
        setDead(false);
        setHasShield(true);

        timers.add(new Timer<>(5f, p -> p.setHasShield(false)));
    }

    public void changeRotationDirection() { rotationDirection *= -1; }

    public void onKeyPressed() {
        setMoving(true);
        changeRotationDirection();
    }

    public void onKeyReleased() {
        setMoving(false);
    }

    @Override
    public void update(double deltaTime) {
        if (isMoving()) move();
        else {
            setRotationAngle(
                    (getRotationAngle() + (getRotationSpeed() * getRotationDirection())) % 360
            );
        }

        List<Timer<Player>> toRemove = new ArrayList<>();
        for (Timer<Player> t: timers) {
            t.update(deltaTime, this, () -> toRemove.add(t));
        }

        timers.removeAll(toRemove);
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) { visitor.visit(this, other); }

    public int getHealth() { return this.health; }
    private void setHealth(int health) { this.health = health; }

    public Effect getActiveEffect() { return this.activeEffect; }
    private void setActiveEffect(Effect effect) { activeEffect = effect; }

    public int getBulletsCount() { return this.bulletsCount; }
    private void setBulletsCount(int bulletsCount) { this.bulletsCount = bulletsCount; }

    public boolean isDead() { return this.isDead; }
    private void setDead(boolean dead) { this.isDead = dead; }

    public int getRotationDirection() { return this.rotationDirection; }

    public float getBulletsReloadDelay() { return this.bulletsReloadDelay; }
    public void setBulletsReloadDelay(float bulletsReloadDelay) { this.bulletsReloadDelay = bulletsReloadDelay; }

    public ShootingStrategy getShootingStrategy() { return this.shootingStrategy; }
    public void setShootingStrategy(ShootingStrategy shootingStrategy) { this.shootingStrategy = shootingStrategy; }

    public int getBulletDamage() { return bulletDamage; }
    public void setBulletDamage(int bulletDamage) { this.bulletDamage = bulletDamage; }

    public float getDefaultSpeed() { return defaultSpeed; }
    private void setDefaultSpeed(float defaultSpeed) { this.defaultSpeed = defaultSpeed; }

    public int getMaxHealth() { return maxHealth; }
    private void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public boolean isHasShield() { return hasShield; }
    public void setHasShield(boolean hasShield) { this.hasShield = hasShield; }

    public int getMaxBulletsCount() { return maxBulletsCount; }
    private void setMaxBulletsCount(int maxBulletsCount) { this.maxBulletsCount = maxBulletsCount; }

    public Tile getBulletTile() { return bulletTile; }
    private void setBulletTile(Tile bulletTile) { this.bulletTile = Objects.requireNonNull(bulletTile); }

    public float getRotationSpeed() { return rotationSpeed; }
    private void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }

    private PlayerSpawner getSpawner() { return spawner; }
    private void setSpawner(PlayerSpawner spawner) { this.spawner = spawner; }

    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { isMoving = moving; }
}
