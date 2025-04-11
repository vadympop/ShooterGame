package com.game.core.entities;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.effects.NoEffect;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.bullet.BulletType;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.shooting.ShootingManager;
import com.game.core.strategies.SingleShootStrategy;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;

import java.util.ArrayList;
import java.util.List;


public class Player extends Entity {
    private PlayerSpawner spawner;
    private int maxHealth;
    private int health;

    private ShootingManager sm;

    private boolean isMoving = false;
    private boolean isDead = false;
    private float defaultSpeed;

    private float rotationSpeed;
    private int rotationDirection = 1;

    private final List<Timer<Player>> timers = new ArrayList<>();
    private Effect activeEffect;
    private boolean hasShield = false;

    public Player(
            PlayerSpawner spawner,
            Bounds hitbox,
            Tile tile,
            SceneConfig.BulletConfig bulletConfig,
            int maxHealth,
            int maxBulletsCount,
            float bulletsReloadDelay,
            float defaultSpeed,
            float rotationSpeed
    ) {
        super(tile, hitbox);

        setSm(new ShootingManager(
                this,
                BulletType.STANDARD,
                new SingleShootStrategy(),
                bulletConfig,
                maxBulletsCount,
                bulletsReloadDelay
        ));
        setMaxHealth(maxHealth);
        setHealth(getMaxHealth());
        setDefaultSpeed(defaultSpeed);
        setSpeed(defaultSpeed);
        setRotationSpeed(rotationSpeed);
        setSpawner(spawner);
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
        getSm().update(deltaTime);

        if (isMoving()) {
            move(deltaTime);
            getSm().toggleShooting(true);
        }
        else {
            float newAngle = (float) (getRotationAngle() + (getRotationSpeed() * getRotationDirection() * deltaTime));
            setRotationAngle(newAngle % 360);
            getSm().toggleShooting(false);
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

    public boolean isDead() { return this.isDead; }
    private void setDead(boolean dead) { this.isDead = dead; }

    public int getRotationDirection() { return this.rotationDirection; }

    public float getDefaultSpeed() { return defaultSpeed; }
    private void setDefaultSpeed(float defaultSpeed) { this.defaultSpeed = defaultSpeed; }

    public int getMaxHealth() { return maxHealth; }
    private void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public boolean isHasShield() { return hasShield; }
    public void setHasShield(boolean hasShield) { this.hasShield = hasShield; }

    public float getRotationSpeed() { return rotationSpeed; }
    private void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }

    private PlayerSpawner getSpawner() { return spawner; }
    private void setSpawner(PlayerSpawner spawner) { this.spawner = spawner; }

    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { isMoving = moving; }

    // Getter for ShootingManager
    public ShootingManager getSm() { return sm; }
    private void setSm(ShootingManager sm) { this.sm = sm; }
}
