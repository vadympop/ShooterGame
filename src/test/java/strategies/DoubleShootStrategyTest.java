package strategies;

import com.game.core.entities.Player;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.bullet.BulletType;
import com.game.core.strategies.DoubleShootStrategy;
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
class DoubleShootStrategyTest {
    @Mock Player mockPlayer;
    @Mock SceneConfig.BulletConfig mockConfig;
    @Mock SceneConfig.BoundsConfig mockHitbox;
    @Mock private Scaler scaler;

    private MockedStatic<Scaler> scalerMock;
    private DoubleShootStrategy strategy;

    @BeforeEach
    void setup() {
        strategy = new DoubleShootStrategy();
        scalerMock = mockStatic(Scaler.class);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);
        when(scaler.getScale()).thenReturn(1f);
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        when(mockHitbox.getType()).thenReturn(BoundsTypeEnum.CIRCLE);
        when(mockHitbox.getRadius()).thenReturn(5f);

        when(mockConfig.getDamage()).thenReturn(20);
        when(mockConfig.getTimeToDestroy()).thenReturn(2f);
        when(mockConfig.getTextures()).thenReturn(Map.of(BulletType.STANDARD, "circle.png"));
        when(mockConfig.getHitbox()).thenReturn(mockHitbox);

        when(mockPlayer.getRotationAngle()).thenReturn(0f);
        when(mockPlayer.getSpeed()).thenReturn(150f);

        strategy.setBulletConfig(mockConfig);
    }

    @AfterEach
    void teardown() {
        scalerMock.close();
    }

    @Test
    void shoot_shouldReturnTwoBulletsWithCorrectProperties() {
        List<Bullet> bullets = strategy.shoot(mockPlayer, BulletType.STANDARD);

        assertEquals(2, bullets.size());
        bullets.forEach(b -> {
            assertEquals(mockPlayer, b.getOwner());
            assertEquals(20, b.getDamage());
            assertEquals(2f, b.getTimeToDestroy(), 0.001f);
        });
    }
}
