package com.game.core.shooting;

import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.strategies.ShootingStrategy;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public class ShootingManager implements Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShootingManager.class);

    private final Queue<DelayedWrapper<Bullet>> bulletsQueue = new LinkedList<>();
    private final Map<Float, Integer> bulletsCountByRotation = new HashMap<>();
    private final Timer<ShootingManager> reloadingTimer;
    private Player player;
    private boolean isShooting = false;
    private int maxBulletsCount;
    private int bulletsCount;
    private float bulletsReloadDelay;
    private float bulletsCooldown;
    private Consumer<Entity> onBulletCreated;
    private BulletType bulletType;
    private SceneConfig.BulletConfig bulletConfig;
    private ShootingStrategy shootingStrategy;

    public ShootingManager(
            Player player,
            BulletType bulletType,
            ShootingStrategy shootingStrategy,
            SceneConfig.BulletConfig bulletConfig,
            int maxBulletsCount,
            float bulletsReloadDelay,
            float bulletsCooldown
    ) {
        setPlayer(player);
        setBulletType(bulletType);
        setBulletConfig(bulletConfig);
        setShootingStrategy(shootingStrategy);
        setBulletsReloadDelay(bulletsReloadDelay);
        setMaxBulletsCount(maxBulletsCount);
        setBulletsCount(maxBulletsCount);
        setBulletsCooldown(bulletsCooldown);

        reloadingTimer = new Timer<>(getBulletsReloadDelay(), (x) -> {
            if (x.getBulletsCount() == x.getMaxBulletsCount()) return;

            x.setBulletsCount(x.getBulletsCount() + 1);

            LOGGER.debug("Reloaded bullets for {}", getPlayer());
        }, true);
    }

    public void toggleShooting(boolean state) {
        if (state == isShooting()) return;
        LOGGER.debug("Toggled shooting for {}", getPlayer());
        isShooting = state;

        if (state) shoot();
    }

    public boolean isShooting() { return isShooting; }

    @Override
    public void update(double deltaTime) {
        reloadingTimer.update(deltaTime, this, null);

        while (!bulletsQueue.isEmpty()) {
            DelayedWrapper<Bullet> shot = bulletsQueue.peek();
            if (shot.canExecute(deltaTime)) bulletsQueue.poll();
            else break;
        }
    }

    private void shoot() {
        if (getBulletsCount() <= 0) return;
        decrementBulletsCount();

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
    private void setMaxBulletsCount(int maxBulletsCount) { this.maxBulletsCount = maxBulletsCount; }

    public int getBulletsCount() { return bulletsCount; }
    private void setBulletsCount(int bulletsCount) { this.bulletsCount = bulletsCount; }
    private void decrementBulletsCount() { setBulletsCount(getBulletsCount() - 1); }

    public float getBulletsReloadDelay() { return bulletsReloadDelay; }
    private void setBulletsReloadDelay(float bulletsReloadDelay) { this.bulletsReloadDelay = bulletsReloadDelay; }

    public BulletType getBulletType() { return bulletType; }
    public void setBulletType(BulletType bulletType) { this.bulletType = bulletType; }

    public SceneConfig.BulletConfig getBulletConfig() { return bulletConfig; }
    public void setBulletConfig(SceneConfig.BulletConfig bulletConfig) { this.bulletConfig = bulletConfig; }

    public ShootingStrategy getShootingStrategy() { return shootingStrategy; }
    public void setShootingStrategy(ShootingStrategy shootingStrategy) {
        LOGGER.info("Set shooting strategy={} for {}", shootingStrategy, getPlayer());
        shootingStrategy.setBulletConfig(getBulletConfig());
        this.shootingStrategy = shootingStrategy;
    }

    public Player getPlayer() { return player; }
    private void setPlayer(Player player) { this.player = player; }

    public Consumer<Entity> getOnBulletCreated() { return onBulletCreated; }
    public void setOnBulletCreated(Consumer<Entity> onBulletCreated) { this.onBulletCreated = onBulletCreated; }

    public float getBulletsCooldown() { return bulletsCooldown; }
    private void setBulletsCooldown(float bulletsCooldown) { this.bulletsCooldown = bulletsCooldown; }
}
