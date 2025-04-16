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
import com.game.core.utils.PositionUtils;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class Player extends Entity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

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
    private final List<Timer<Player>> timersToAdd = new ArrayList<>();

    private Effect activeEffect;
    private boolean hasShield = false;
    private float shieldHitboxMultiplier;

    public Player(
            PlayerSpawner spawner,
            Bounds hitbox,
            Tile tile,
            SceneConfig.BulletConfig bulletConfig,
            int maxHealth,
            int maxBulletsCount,
            float shieldHitboxMultiplier,
            float bulletsReloadDelay,
            float bulletsCooldown,
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
                bulletsReloadDelay,
                bulletsCooldown
        ));
        setMaxHealth(maxHealth);
        setHealth(getMaxHealth());
        setShieldHitboxMultiplier(shieldHitboxMultiplier);
        setDefaultSpeed(defaultSpeed);
        setSpeed(defaultSpeed);
        setRotationSpeed(rotationSpeed);
        setSpawner(spawner);
    }

    public boolean applyEffect(Effect effect) {
        LOGGER.info("Apply effect {}", effect);
        if (getActiveEffect() != null || getActiveEffect() instanceof NoEffect) return false;

        setActiveEffect(effect);
        effect.apply(this);

        timersToAdd.add(new Timer<>(effect.getDuration(), (x) -> {
            LOGGER.info("Effect removed");
            x.getActiveEffect().remove(x);
            x.setActiveEffect(null);
        }));

        LOGGER.info("Effect applied = {}", effect);
        return true;
    }

    public void takeDamage(int damage) {
        if (isHasShield() || isDead()) return;

        this.setHealth(this.getHealth() - damage);
        if (this.getHealth() <= 0) {
            LOGGER.info("Took critical damage, player is dead");
            setDead(true);
            timersToAdd.add(new Timer<>(5f, (p) -> p.getSpawner().spawn()));
        }
    }

    public void respawn(float x, float y) {
        LOGGER.info("Player respawned");

        setPos(x, y);
        setHealth(getMaxHealth());
        setDead(false);
        setHasShield(true);

        timersToAdd.add(new Timer<>(5f, p -> p.setHasShield(false)));
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
        List<Timer<Player>> toRemove = new ArrayList<>();
        timers.forEach(t -> t.update(deltaTime, this, () -> toRemove.add(t)));
        timers.removeAll(toRemove);
        timers.addAll(timersToAdd);
        timersToAdd.clear();

        getSm().update(deltaTime);

        // If player is dead it's not rotating and not moving
        if(isDead()) return;

        if (isMoving()) {
            move(deltaTime);
            getSm().toggleShooting(true);
            return;
        }

        // Rotate while player is not moving
        float newAngle = (float) (getRotationAngle() + (getRotationSpeed() * getRotationDirection() * deltaTime));
        setRotationAngle(newAngle % 360);
        getSm().toggleShooting(false);
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) { visitor.visit(this, other); }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);

        if (isHasShield()) drawEffectCircle(gc, Color.web("#949494", 0.5f));
    }

    public void drawEffectCircle(GraphicsContext gc, Color color) {
        float maxSize = getHitbox().getMaxSize();
        double[] displayPos = PositionUtils.generateDisplayPos(getX(), getY(), getHitbox());

        gc.setFill(color);
        gc.fillOval(displayPos[0], displayPos[1], maxSize, maxSize);
    }

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
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
        float multiplier = 1f;
        if (hasShield) multiplier = getShieldHitboxMultiplier();

        getHitbox().multiply(multiplier);
    }

    public float getRotationSpeed() { return rotationSpeed; }
    private void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }

    private PlayerSpawner getSpawner() { return spawner; }
    private void setSpawner(PlayerSpawner spawner) { this.spawner = spawner; }

    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { isMoving = moving; }

    // Getter for ShootingManager
    public ShootingManager getSm() { return sm; }
    private void setSm(ShootingManager sm) { this.sm = sm; }

    public float getShieldHitboxMultiplier() { return shieldHitboxMultiplier; }
    public void setShieldHitboxMultiplier(float shieldHitboxMultiplier) { this.shieldHitboxMultiplier = shieldHitboxMultiplier; }
}
