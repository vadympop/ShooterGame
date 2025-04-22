package factories;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.effects.*;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.factories.BonusFactory;
import com.game.core.factories.BoundsFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.ConfigLoader;
import com.game.core.utils.config.SceneConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BonusFactoryTest {
    @Mock private SceneConfig config;
    @Mock private SceneConfig.BonusConfig bonusConfig;
    @Mock private SceneConfig.BoundsConfig boundsConfig;
    @Mock private ConfigLoader configLoader;
    @Mock private RectangleBounds hitbox;
    @Mock private Scaler scaler;
    private MockedStatic<ConfigLoader> configLoaderMock;
    private MockedStatic<Scaler> scalerMock;
    private MockedStatic<BoundsFactory> boundsFactoryMock;

    @BeforeEach
    void setUp() {
        configLoaderMock = mockStatic(ConfigLoader.class);
        boundsFactoryMock = mockStatic(BoundsFactory.class);
        scalerMock = mockStatic(Scaler.class);

        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);
        when(configLoader.getConfig()).thenReturn(config);

        configLoaderMock.when(ConfigLoader::getInstance).thenReturn(configLoader);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);

        when(config.getBonus()).thenReturn(bonusConfig);
        when(bonusConfig.getTexture()).thenReturn("bonus.png");
        when(bonusConfig.getHitbox()).thenReturn(boundsConfig);
        boundsFactoryMock.when(() -> BoundsFactory.createForBlock(boundsConfig)).thenReturn(hitbox);
    }

    @AfterEach
    void tearDown() {
        configLoaderMock.close();
        boundsFactoryMock.close();
        scalerMock.close();
    }

    @Test
    void createShieldBonusReturnsBonusWithShieldEffect() {
        Bonus bonus = BonusFactory.create(BonusType.SHIELD);

        assertNotNull(bonus);
        assertInstanceOf(ShieldEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }

    @Test
    void createSpeedBoostBonusReturnsBonusWithSpeedBoostEffect() {
        Bonus bonus = BonusFactory.create(BonusType.SPEED_BOOST);

        assertNotNull(bonus);
        assertInstanceOf(SpeedBoostEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }

    @Test
    void createDoubleShootBonusReturnsBonusWithDoubleShootEffect() {
        Bonus bonus = BonusFactory.create(BonusType.DOUBLE_SHOOT);

        assertNotNull(bonus);
        assertInstanceOf(DoubleShootEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }

    @Test
    void createDoubleDamageBonusReturnsBonusWithDoubleDamageEffect() {
        Bonus bonus = BonusFactory.create(BonusType.DOUBLE_DAMAGE);

        assertNotNull(bonus);
        assertInstanceOf(DoubleDamageEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }

    @Test
    void createCircularShootBonusReturnsBonusWithCircularShootEffect() {
        Bonus bonus = BonusFactory.create(BonusType.CIRCULAR_SHOOT);

        assertNotNull(bonus);
        assertInstanceOf(CircularShootEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }

    @Test
    void createWithNullBonusTypeReturnsBonusWithNoEffect() {
        Bonus bonus = BonusFactory.create(null);

        assertNotNull(bonus);
        assertInstanceOf(NoEffect.class, bonus.getEffect());
        assertEquals(hitbox, bonus.getHitbox());
        verify(config, times(2)).getBonus();
        verify(bonusConfig).getTexture();
        verify(bonusConfig).getHitbox();
        boundsFactoryMock.verify(() -> BoundsFactory.createForBlock(boundsConfig));
    }
}