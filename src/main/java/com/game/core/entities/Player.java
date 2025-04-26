package com.game.core.entities;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.effects.NoEffect;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.entities.bullet.BulletType;
import com.game.core.exceptions.InvalidParameterException;
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
import java.util.Objects;


/**
 * Represents a player in the game with abilities such as movement, shooting, respawning,
 * and applying effects. Manages player properties like health, shield, and kills count.
 */
public class Player extends Entity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final PlayerSpawner spawner;
    private int maxHealth;
    private int health;

    private final ShootingManager sm;

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
    private int killsCount = 0;

    /**
     * Constructs a Player instance with specified parameters.
     *
     * @param spawner                The PlayerSpawner responsible for spawning this player.
     * @param hitbox                 The bounding box for collision detection.
     * @param tile                   The graphical tile representing the player.
     * @param bulletConfig           Configuration for the player's bullets.
     * @param maxHealth              The maximum health of the player.
     * @param maxBulletsCount        Maximum number of bullets the player can store.
     * @param shieldHitboxMultiplier Multiplier for the hitbox size when the shield is active.
     * @param bulletsReloadDelay     The delay before reloading bullets.
     * @param bulletsCooldown        The cooldown time between shooting bullets.
     * @param defaultSpeed           The default movement speed of the player.
     * @param rotationSpeed          The speed of rotation for the player.
     * @param isInfinityBulletsMode  Specifies if the player has infinite bullets.
     */
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
            float rotationSpeed,
            boolean isInfinityBulletsMode
    ) {
        super(tile, hitbox);

        this.sm = new ShootingManager(
                this,
                BulletType.STANDARD,
                new SingleShootStrategy(),
                bulletConfig,
                maxBulletsCount,
                bulletsReloadDelay,
                bulletsCooldown,
                isInfinityBulletsMode
        );
        this.spawner = Objects.requireNonNull(spawner);
        setMaxHealth(maxHealth);
        setHealth(getMaxHealth());
        setShieldHitboxMultiplier(shieldHitboxMultiplier);
        setDefaultSpeed(defaultSpeed);
        setSpeed(defaultSpeed);
        setRotationSpeed(rotationSpeed);
    }

    /**
     * Applies a specified effect to the player if no other active effect exists.
     *
     * @param effect The effect to be applied.
     * @return True if the effect is successfully applied, false otherwise.
     */
    public boolean applyEffect(Effect effect) {
        LOGGER.info("Apply effect {}", effect.getClass().getSimpleName());
        if (getActiveEffect() != null || getActiveEffect() instanceof NoEffect) return false;

        setActiveEffect(effect);
        effect.apply(this);

        timersToAdd.add(new Timer<>(effect.getDuration(), (x) -> {
            LOGGER.info("Effect removed");
            x.getActiveEffect().remove(x);
            x.setActiveEffect(null);
        }));

        LOGGER.info("Effect applied = {}", effect.getClass().getSimpleName());
        return true;
    }

    /**
     * Reduces the player's health by the specified damage amount and marks the player as dead if health falls below zero.
     *
     * @param damage   The amount of damage to inflict.
     * @param attacker The player attacking this player (null if there is no attacker).
     */
    public void takeDamage(int damage, Player attacker) {
        if (isHasShield() || isDead()) return;

        this.setHealth(this.getHealth() - damage);
        if (this.getHealth() <= 0) {
            LOGGER.info("Took critical damage, player is dead");
            setDead(true);
            if (attacker != null) attacker.incrementKillsCount();
            timersToAdd.add(new Timer<>(5f, (p) -> p.getSpawner().spawn()));
        }
    }

    /**
     * Respawns the player at the specified position with full health and temporary shield.
     *
     * @param x The x-coordinate for respawning.
     * @param y The y-coordinate for respawning.
     */
    public void respawn(float x, float y) {
        LOGGER.info("Player respawned");

        setPos(x, y);
        setHealth(getMaxHealth());
        setDead(false);
        setHasShield(true);

        timersToAdd.add(new Timer<>(5f, p -> p.setHasShield(false)));
    }

    /**
     * Increments the player's kills count by one.
     */
    public void incrementKillsCount() {
        killsCount++;
    }

    /**
     * Changes the player's rotation direction to the opposite direction.
     */
    public void changeRotationDirection() {
        rotationDirection *= -1;
    }

    /**
     * Handles behavior when a key is pressed, including starting movement and changing rotation direction.
     */
    public void onKeyPressed() {
        setMoving(true);
        changeRotationDirection();
    }

    /**
     * Stops player movement when the key is released.
     */
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

    /**
     * Handles collisions with other objects by invoking the collision visitor's logic.
     *
     * @param visitor The collision visitor handling the collision logic.
     * @param other   The other collidable object involved in the collision.
     */
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    /**
     * Renders the player on the game canvas, including a visual effect if the shield is active.
     *
     * @param gc The graphics context for rendering.
     */
    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);

        if (isHasShield()) drawEffectCircle(gc, Color.web("#949494", 0.5f));
        if (hasSmallHealth()) drawEffectCircle(gc, Color.web("#b52218", 0.5f));
    }

    /**
     * Draws a circular visual effect around the player.
     *
     * @param gc    The graphics context for rendering the circle.
     * @param color The color of the effect circle.
     */
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

    public boolean hasSmallHealth() { return getHealth() <= getMaxHealth() * 0.5; }

    public int getMaxHealth() { return maxHealth; }
    private void setMaxHealth(int maxHealth) {
        if (maxHealth <= 0) throw new InvalidParameterException("Player's max health must be higher than 0");

        this.maxHealth = maxHealth;
    }

    /**
     * Checks if the player currently has a shield.
     *
     * @return True if the shield is active, false otherwise.
     */
    public boolean isHasShield() {
        return hasShield;
    }

    /**
     * Activates or deactivates the player's shield and adjusts the hitbox size accordingly.
     *
     * @param hasShield True to activate the shield, false to deactivate it.
     */
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
        float multiplier = 1f;
        if (hasShield) multiplier = getShieldHitboxMultiplier();

        getHitbox().multiply(multiplier);
    }

    public float getRotationSpeed() { return rotationSpeed; }
    private void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }

    private PlayerSpawner getSpawner() { return spawner; }

    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { isMoving = moving; }

    /**
     * Retrieves the shooting manager responsible for handling the player's shooting behavior.
     *
     * @return The ShootingManager instance.
     */
    public ShootingManager getSm() {
        return sm;
    }

    public float getShieldHitboxMultiplier() { return shieldHitboxMultiplier; }
    public void setShieldHitboxMultiplier(float shieldHitboxMultiplier) {
        if (shieldHitboxMultiplier <= 0)
            throw new InvalidParameterException("Player's shield hitbox multiplier must be higher than 0");

        this.shieldHitboxMultiplier = shieldHitboxMultiplier;
    }

    public int getKillsCount() { return killsCount; }

    @Override
    public String toString() {
        return "Player{" +
                "maxHealth=" + maxHealth +
                ", health=" + health +
                ", isMoving=" + isMoving +
                ", isDead=" + isDead +
                ", defaultSpeed=" + defaultSpeed +
                ", rotationSpeed=" + rotationSpeed +
                ", rotationDirection=" + rotationDirection +
                ", timers=" + timers.size() +
                ", activeEffect=" + (activeEffect != null ? activeEffect.getClass().getSimpleName() : "null") +
                ", hasShield=" + hasShield +
                ", shieldHitboxMultiplier=" + shieldHitboxMultiplier +
                "}->" + super.toString();
    }
}
