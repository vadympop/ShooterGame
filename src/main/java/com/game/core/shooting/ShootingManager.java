package com.game.core.shooting;

import com.game.core.behaviour.interfaces.Updatable;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.strategies.ShootingStrategy;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class ShootingManager implements Updatable {
    private final Queue<DelayedWrapper<Bullet>> bulletsQueue = new LinkedList<>();
    private final Timer<ShootingManager> reloadingTimer;
    private Player player;
    private boolean isShooting = false;
    private int maxBulletsCount;
    private int bulletsCount;
    private float bulletsReloadDelay;
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
            float bulletsReloadDelay
    ) {
        setPlayer(player);
        setBulletType(bulletType);
        setBulletConfig(bulletConfig);
        setShootingStrategy(shootingStrategy);
        setBulletsReloadDelay(bulletsReloadDelay);
        setMaxBulletsCount(maxBulletsCount);
        setBulletsCount(maxBulletsCount);

        reloadingTimer = new Timer<>(getBulletsReloadDelay(), (x) -> {
            if (x.getBulletsCount() == x.getMaxBulletsCount()) return;

            x.setBulletsCount(x.getBulletsCount() + 1);
        }, true);
    }

    public void toggleShooting(boolean state) {
        if (state == isShooting()) return;
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
            double delay = bulletsQueue.size() * 0.4;
            bulletsQueue.add(new DelayedWrapper<>(
                    delay,
                    bullet,
                    b -> {
                        b.setStartPosition();
                        getOnBulletCreated().accept(b);
                    }
            ));
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
        shootingStrategy.setBulletConfig(getBulletConfig());
        this.shootingStrategy = shootingStrategy;
    }

    public Player getPlayer() { return player; }
    private void setPlayer(Player player) { this.player = player; }

    public Consumer<Entity> getOnBulletCreated() { return onBulletCreated; }
    public void setOnBulletCreated(Consumer<Entity> onBulletCreated) { this.onBulletCreated = onBulletCreated; }
}
