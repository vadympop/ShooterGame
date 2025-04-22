package factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.entities.Player;
import com.game.core.factories.BoundsFactory;
import com.game.core.factories.PlayerFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.spawners.PlayerSpawner;
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
public class PlayerFactoryTest {
    @Mock private PlayerSpawner spawner;
    @Mock private Tile playerTile;
    @Mock private SceneConfig config;
    @Mock private SceneConfig.PlayerConfig playerConfig;
    @Mock private SceneConfig.BulletConfig bulletConfig;
    @Mock private SceneConfig.BoundsConfig boundsConfig;
    @Mock private ConfigLoader configLoader;
    @Mock private Bounds hitbox;
    @Mock private Scaler scaler;

    private MockedStatic<ConfigLoader> configLoaderMock;
    private MockedStatic<Scaler> scalerMock;
    private MockedStatic<BoundsFactory> boundsFactoryMock;

    @BeforeEach
    void setup() {
        configLoaderMock = mockStatic(ConfigLoader.class);
        boundsFactoryMock = mockStatic(BoundsFactory.class);
        scalerMock = mockStatic(Scaler.class);

        configLoaderMock.when(ConfigLoader::getInstance).thenReturn(configLoader);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);

        boundsFactoryMock.when(() -> BoundsFactory.createFromConfig(boundsConfig)).thenReturn(hitbox);
    }

    void configurePlayer() {
        when(configLoader.getConfig()).thenReturn(config);
        when(spawner.getPlayerTile()).thenReturn(playerTile);
        when(config.getPlayer()).thenReturn(playerConfig);
        when(config.getBullet()).thenReturn(bulletConfig);
        when(playerConfig.getHitbox()).thenReturn(boundsConfig);
        when(playerConfig.getMaxHealth()).thenReturn(100);
        when(playerConfig.getMaxBulletsCount()).thenReturn(5);
        when(playerConfig.getShieldHitboxMultiplier()).thenReturn(1.2f);
        when(playerConfig.getBulletsReloadDelay()).thenReturn(0.5f);
        when(playerConfig.getBulletsCooldown()).thenReturn(0.2f);
        when(playerConfig.getSpeed()).thenReturn(100f);
        when(playerConfig.getRotationSpeed()).thenReturn(3f);
    }

    @AfterEach
    void teardown() {
        configLoaderMock.close();
        boundsFactoryMock.close();
        scalerMock.close();
    }

    @Test
    void createPlayerReturnsConfiguredPlayer() {
        configurePlayer();

        Player player = PlayerFactory.create(spawner);

        assertNotNull(player);
        assertEquals(100, player.getHealth());
        assertEquals(hitbox, player.getHitbox());
        assertEquals(playerTile, player.getTile());
        assertEquals(bulletConfig, player.getSm().getBulletConfig());
        assertEquals(5, player.getSm().getMaxBulletsCount());
        assertEquals(1.2f, player.getShieldHitboxMultiplier());
        assertEquals(0.5f, player.getSm().getBulletsReloadDelay());
        assertEquals(0.2f, player.getSm().getBulletsCooldown());
        assertEquals(100f, player.getSpeed());
        assertEquals(3f, player.getRotationSpeed());
        verify(spawner).getPlayerTile();
        verify(config, atLeastOnce()).getPlayer();
        verify(config).getBullet();
        verify(playerConfig).getHitbox();
        verify(playerConfig).getMaxHealth();
        verify(playerConfig).getMaxBulletsCount();
        verify(playerConfig).getShieldHitboxMultiplier();
        verify(playerConfig).getBulletsReloadDelay();
        verify(playerConfig).getBulletsCooldown();
        verify(playerConfig).getSpeed();
        verify(playerConfig).getRotationSpeed();
        boundsFactoryMock.verify(() -> BoundsFactory.createFromConfig(boundsConfig));
    }

    @Test
    void createWithNullSpawnerThrowsIllegalArgumentException() {
        assertThrows(
                NullPointerException.class,
                () -> PlayerFactory.create(null)
        );
        verifyNoInteractions(spawner, config, playerConfig, bulletConfig, boundsConfig);
        boundsFactoryMock.verifyNoInteractions();
    }
}