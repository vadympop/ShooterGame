package strategies;

import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.entities.Player;
import com.game.core.strategies.SingleShootStrategy;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleShootStrategyTest {
    @Mock Player mockPlayer;
    @Mock SceneConfig.BulletConfig mockConfig;
    @Mock SceneConfig.BoundsConfig mockHitboxConfig;
    @Mock private Scaler scaler;

    private MockedStatic<Scaler> scalerMock;
    private SingleShootStrategy strategy;

    @BeforeEach
    void setup() {
        strategy = new SingleShootStrategy();
        scalerMock = mockStatic(Scaler.class);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);
        when(scaler.getScale()).thenReturn(1f);
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        when(mockHitboxConfig.getType()).thenReturn(BoundsTypeEnum.CIRCLE);
        when(mockHitboxConfig.getRadius()).thenReturn(5f);

        when(mockConfig.getDamage()).thenReturn(25);
        when(mockConfig.getTimeToDestroy()).thenReturn(1.5f);
        when(mockConfig.getTextures()).thenReturn(Map.of(BulletType.STANDARD, "texture.png"));
        when(mockConfig.getHitbox()).thenReturn(mockHitboxConfig);

        when(mockPlayer.getRotationAngle()).thenReturn(45f);
        when(mockPlayer.getSpeed()).thenReturn(100f);

        strategy.setBulletConfig(mockConfig);
    }

    @AfterEach
    void teardown() {
        scalerMock.close();
    }

    @Test
    void shoot_shouldReturnOneBulletWithCorrectConfig() {
        List<Bullet> bullets = strategy.shoot(mockPlayer, BulletType.STANDARD);

        assertEquals(1, bullets.size());
        Bullet bullet = bullets.getFirst();

        assertEquals(mockPlayer, bullet.getOwner());
        assertEquals(BulletType.STANDARD, bullet.getType());
        assertEquals(25, bullet.getDamage());
        assertEquals(1.5f, bullet.getTimeToDestroy(), 0.01f);
        assertNotNull(bullet.getDestroyTimer());
    }
}
