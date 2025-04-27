package com.game.core.shooting;

import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.strategies.ShootingStrategy;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages the shooting behavior of a player in the game, including bullet creation, cooldowns,
 * reloading logic, and shooting strategies. This class implements {@link Updatable}
 * to allow regular updates tied to game events or timers.
 */
public class ShootingManager implements Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingManager.class);

    private final Queue<DelayedWrapper<Bullet>> bulletsQueue = new LinkedList<>();
    private final Map<Float, Integer> bulletsCountByRotation = new HashMap<>();
    private final Timer<ShootingManager> reloadingTimer;
    private final Player player;
    private boolean isShooting = false;
    private final boolean isInfinityBulletsMode;
    private int maxBulletsCount;
    private int bulletsCount;
    private final float bulletsReloadDelay;
    private final float bulletsCooldown;
    private Consumer<Entity> onBulletCreated;
    private BulletType bulletType;
    private final SceneConfig.BulletConfig bulletConfig;
    private ShootingStrategy shootingStrategy;

    /**
     * Creates a new ShootingManager instance with the specified parameters.
     *
     * @param player                The player entity associated with this shooting manager.
     * @param bulletType            The type of bullets being shot.
     * @param shootingStrategy      The shooting strategy used for firing bullets.
     * @param bulletConfig          Configuration settings for bullets.
     * @param maxBulletsCount       The maximum number of bullets that can be held.
     * @param bulletsReloadDelay    The delay between reloading bullets.
     * @param bulletsCooldown       The cooldown time between consecutive bullet firings.
     * @param isInfinityBulletsMode Specifies if infinite bullets mode is enabled.
     */
    public ShootingManager(
            Player player,
            BulletType bulletType,
            ShootingStrategy shootingStrategy,
            SceneConfig.BulletConfig bulletConfig,
            int maxBulletsCount,
            float bulletsReloadDelay,
            float bulletsCooldown,
            boolean isInfinityBulletsMode
    ) {
        this.player = Objects.requireNonNull(player);
        this.bulletsCooldown = bulletsCooldown;
        this.bulletConfig = Objects.requireNonNull(bulletConfig);
        this.bulletsReloadDelay = bulletsReloadDelay;
        this.isInfinityBulletsMode = isInfinityBulletsMode;

        setBulletType(bulletType);
        setShootingStrategy(shootingStrategy);
        setMaxBulletsCount(maxBulletsCount);
        setBulletsCount(maxBulletsCount);

        if (!isInfinityBulletsMode())
            reloadingTimer = new Timer<>(getBulletsReloadDelay(), (x) -> {
                if (x.getBulletsCount() == x.getMaxBulletsCount()) return;

                x.setBulletsCount(x.getBulletsCount() + 1);

                LOGGER.debug("Reloaded bullets for {}", getPlayer());
            }, true);
        else
            reloadingTimer = null;
    }

    /**
     * Toggles the shooting state for the player. When enabled, this will initiate shooting
     * based on the shooting strategy and game configuration.
     *
     * @param state {@code true} to enable shooting, {@code false} to disable.
     */
    public void toggleShooting(boolean state) {
        if (state == isShooting()) return;
        LOGGER.debug("Toggled shooting for {}", getPlayer());
        isShooting = state;

        if (state) shoot();
    }

    public boolean isShooting() { return isShooting; }

    /**
     * Updates the state of the shooting manager, including handling reloading timers and bullet queues.
     *
     * @param deltaTime The time difference since the last update, used for timing calculations.
     */
    @Override
    public void update(double deltaTime) {
        if (reloadingTimer != null)
            reloadingTimer.update(deltaTime, this, null);

        while (!bulletsQueue.isEmpty()) {
            DelayedWrapper<Bullet> shot = bulletsQueue.peek();
            if (shot.canExecute(deltaTime)) bulletsQueue.poll();
            else break;
        }
    }

    /**
     * Fires bullets based on the player's current shooting strategy and game configuration.
     * If infinite bullets mode is disabled, the method ensures there are enough bullets to fire,
     * decrementing the bullet count accordingly. Bullets are created using the associated
     * {@link ShootingStrategy}, and their firing is delayed based on the cooldown configuration.
     * Each fired bullet is added to the queue for delayed execution, where its creation logic
     * and rotation-dependent handling is processed.
     */
    private void shoot() {
        if (!isInfinityBulletsMode) {
            if (getBulletsCount() <= 0) return;
            decrementBulletsCount();
        }

        List<Bullet> bullets = getShootingStrategy().shoot(getPlayer(), getBulletType());
        for (Bullet bullet : bullets) {
            float bulletRotation = bullet.getRotationAngle();
            bulletsCountByRotation.putIfAbsent(bulletRotation, 0);
            int count = bulletsCountByRotation.get(bulletRotation);

            double delay = count * getBulletsCooldown();
            bulletsQueue.add(new DelayedWrapper<>(
                    delay,
                    bullet,
                    b -> {
                        b.setStartPosition();
                        getOnBulletCreated().accept(b);
                        bulletsCountByRotation.computeIfPresent(
                                bulletRotation,
                                (key, oldValue) -> Math.max(0, oldValue - 1)
                        );
                    }
            ));

            bulletsCountByRotation.computeIfPresent(bulletRotation, (key, oldValue) -> oldValue + 1);
        }
    }

    public int getMaxBulletsCount() { return maxBulletsCount; }
    private void setMaxBulletsCount(int maxBulletsCount) {
        if (maxBulletsCount <= 0) throw new InvalidParameterException("Max bullets count must be higher than 0");

        this.maxBulletsCount = maxBulletsCount;
    }

    public int getBulletsCount() { return bulletsCount; }
    private void setBulletsCount(int bulletsCount) { this.bulletsCount = bulletsCount; }
    private void decrementBulletsCount() { setBulletsCount(getBulletsCount() - 1); }

    public BulletType getBulletType() { return bulletType; }
    public void setBulletType(BulletType bulletType) { this.bulletType = bulletType; }

    public ShootingStrategy getShootingStrategy() { return shootingStrategy; }

    /**
     * Sets a new shooting strategy for the player. This defines how bullets are fired.
     *
     * @param shootingStrategy The new {@link ShootingStrategy} to use.
     */
    public void setShootingStrategy(ShootingStrategy shootingStrategy) {
        LOGGER.info("Set shooting strategy={} for {}", shootingStrategy.getClass().getSimpleName(), getPlayer());
        shootingStrategy.setBulletConfig(getBulletConfig());
        this.shootingStrategy = shootingStrategy;
    }

    public Consumer<Entity> getOnBulletCreated() { return onBulletCreated; }
    public void setOnBulletCreated(Consumer<Entity> onBulletCreated) { this.onBulletCreated = onBulletCreated; }

    public Player getPlayer() { return player; }
    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
    public float getBulletsCooldown() { return bulletsCooldown; }
    public float getBulletsReloadDelay() { return bulletsReloadDelay; }
    public boolean isInfinityBulletsMode() { return isInfinityBulletsMode; }
}
