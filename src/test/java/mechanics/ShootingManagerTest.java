package mechanics;

import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.shooting.ShootingManager;
import com.game.core.strategies.ShootingStrategy;
import com.game.core.utils.Timer;
import com.game.core.utils.config.SceneConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ShootingManagerTest {
    private ShootingManager shootingManager;
    @Mock private Player player;
    @Mock private BulletType bulletType;
    @Mock private ShootingStrategy shootingStrategy;
    @Mock private SceneConfig.BulletConfig bulletConfig;
    @Mock private Consumer<Entity> onBulletCreated;
    @Mock private Timer<ShootingManager> timer;
    @Mock private Bullet bullet;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        shootingManager = new ShootingManager(
                player,
                bulletType,
                shootingStrategy,
                bulletConfig,
                10, // maxBulletsCount
                1.0f, // bulletsReloadDelay
                0.1f, // bulletsCooldown
                false
        );

        // Inject timer mock via reflection
        Field timerField = ShootingManager.class.getDeclaredField("reloadingTimer");
        timerField.setAccessible(true);
        timerField.set(shootingManager, timer);

        shootingManager.setOnBulletCreated(onBulletCreated);
    }

    @Test
    void constructorInitializesCorrectly() {
        assertEquals(player, shootingManager.getPlayer());
        assertEquals(bulletType, shootingManager.getBulletType());
        assertEquals(shootingStrategy, shootingManager.getShootingStrategy());
        assertEquals(bulletConfig, shootingManager.getBulletConfig());
        assertEquals(10, shootingManager.getMaxBulletsCount());
        assertEquals(10, shootingManager.getBulletsCount());
        assertEquals(1.0f, shootingManager.getBulletsReloadDelay());
        assertEquals(0.1f, shootingManager.getBulletsCooldown());
        assertFalse(shootingManager.isShooting());
        verify(shootingStrategy).setBulletConfig(bulletConfig);
    }

    @Test
    void constructorThrowsOnNullPlayer() {
        assertThrows(
                NullPointerException.class,
                () -> new ShootingManager(
                        null,
                        bulletType,
                        shootingStrategy,
                        bulletConfig,
                        10,
                        1.0f,
                        0.1f,
                        false
                )
        );
    }

    @Test
    void constructorThrowsOnNullBulletConfig() {
        assertThrows(
                NullPointerException.class,
                () -> new ShootingManager(
                        player,
                        bulletType,
                        shootingStrategy,
                        null,
                        10,
                        1.0f,
                        0.1f,
                        false
                )
        );
    }

    @Test
    void constructorWithInfinityBulletsMode() {
        ShootingManager sm = new ShootingManager(
                player,
                bulletType,
                shootingStrategy,
                bulletConfig,
                10,
                1.0f,
                0.1f,
                true
        );

        try {
            Field timerField = ShootingManager.class.getDeclaredField("reloadingTimer");
            timerField.setAccessible(true);
            Object timer = timerField.get(sm);

            // if isInfinityBulletsMode=true, reloadingTimer must be null
            assertNull(timer);
        } catch (Exception e) {
            fail("Unable to use reflection");
        }
    }

    @Test
    void setMaxBulletsCountThrowsOnNonPositiveValue() {
        assertThrows(
                InvalidParameterException.class,
                () -> new ShootingManager(
                        player,
                        bulletType,
                        shootingStrategy,
                        bulletConfig,
                        0,
                        1.0f,
                        0.1f,
                        false
                )
        );
        assertThrows(
                InvalidParameterException.class,
                () -> new ShootingManager(
                        player,
                        bulletType,
                        shootingStrategy,
                        bulletConfig,
                        -1,
                        1.0f,
                        0.1f,
                        false
                )
        );
    }

    @Test
    void toggleShootingEnablesShooting() {
        when(shootingStrategy.shoot(player, bulletType)).thenReturn(List.of(bullet));
        when(bullet.getRotationAngle()).thenReturn(0.0f);

        shootingManager.toggleShooting(true);
        assertTrue(shootingManager.isShooting());
        assertEquals(9, shootingManager.getBulletsCount());
        verify(shootingStrategy).shoot(player, bulletType);
        verify(bullet).getRotationAngle();
    }

    @Test
    void toggleShootingDisablesShooting() {
        shootingManager.toggleShooting(true);
        verify(shootingStrategy).shoot(any(), any());

        shootingManager.toggleShooting(false);
        assertFalse(shootingManager.isShooting());
    }

    @Test
    void toggleShootingNoOpIfSameState() {
        shootingManager.toggleShooting(false);
        assertFalse(shootingManager.isShooting());
        verify(shootingStrategy, never()).shoot(any(), any());
    }

    @Test
    void shootDoesNothingIfNoBullets() {
        shootingManager = new ShootingManager(
                player,
                bulletType,
                shootingStrategy,
                bulletConfig,
                1,
                1.0f,
                0.1f,
                false
        );

        shootingManager.setOnBulletCreated(onBulletCreated);
        shootingManager.toggleShooting(true); // Uses 1 bullet
        shootingManager.toggleShooting(true); // No bullets left
        assertEquals(0, shootingManager.getBulletsCount());
        verify(shootingStrategy, times(1)).shoot(player, bulletType);
    }

    @Test
    void shootCreatesBulletsWithDelay() {
        when(shootingStrategy.shoot(player, bulletType)).thenReturn(List.of(bullet));
        when(bullet.getRotationAngle()).thenReturn(0.0f);

        shootingManager.toggleShooting(true);
        assertEquals(9, shootingManager.getBulletsCount());

        verify(bullet).getRotationAngle();
        // Cannot directly verify queue contents, but we can check side effects later
    }

    @Test
    void updateProcessesTimerAndQueue() {
        // Simulate adding to queue
        shootingManager.toggleShooting(true); // Ensure queue is populated
        shootingManager.update(0.1);
        verify(timer).update(eq(0.1), eq(shootingManager), isNull());
    }

    @Test
    void reloadingTimerIncrementsBulletsCount() {
        shootingManager = new ShootingManager(
                player,
                bulletType,
                shootingStrategy,
                bulletConfig,
                10,
                1.0f,
                0.1f,
                false
        );
        shootingManager.setOnBulletCreated(onBulletCreated);
        when(shootingStrategy.shoot(player, bulletType)).thenReturn(List.of(bullet));
        when(bullet.getRotationAngle()).thenReturn(0.0f);

        shootingManager.toggleShooting(true); // Uses 1 bullet
        assertEquals(9, shootingManager.getBulletsCount());

        shootingManager.update(1.0);
        assertEquals(10, shootingManager.getBulletsCount());
    }

    @Test
    void setShootingStrategyUpdatesConfig() {
        ShootingStrategy newStrategy = mock(ShootingStrategy.class);
        shootingManager.setShootingStrategy(newStrategy);
        assertEquals(newStrategy, shootingManager.getShootingStrategy());
        verify(newStrategy).setBulletConfig(bulletConfig);
    }

    @Test
    void setOnBulletCreatedTriggersOnShoot() {
        when(shootingStrategy.shoot(player, bulletType)).thenReturn(List.of(bullet));
        when(bullet.getRotationAngle()).thenReturn(0.0f);
        shootingManager.setOnBulletCreated(onBulletCreated);

        shootingManager.toggleShooting(true);
        // Bullet is added to queue, not immediately processed
        verifyNoInteractions(onBulletCreated);
    }
}