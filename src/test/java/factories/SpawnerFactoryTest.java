package factories;

import com.game.core.entities.Entity;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.factories.SpawnerFactory;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.SpawnerTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpawnerFactoryTest {
    @Mock private SceneConfig.SpawnerConfig config;
    @Mock private Scaler mockScaler;
    private MockedStatic<ConfigManager> configLoaderMock;

    @BeforeEach
    void setup() {
        configLoaderMock = mockStatic(ConfigManager.class);
    }

    @AfterEach
    void teardown() {
        configLoaderMock.close();
    }

    @Test
    void createBonusSpawnerReturnsConfiguredBonusSpawner() {
        try (MockedStatic<Scaler> mocked = mockStatic(Scaler.class)) {
            mocked.when(Scaler::getInstance).thenReturn(mockScaler);
            when(mockScaler.getTileWidth()).thenReturn(32f);
            when(mockScaler.getTileHeight()).thenReturn(32f);

            when(config.getType()).thenReturn(SpawnerTypeEnum.BONUS);
            when(config.getTexture()).thenReturn("bonus.png");
            when(config.getCooldown()).thenReturn(5f);
            Map<String, Consumer<Entity>> events = new HashMap<>();
            Consumer<Entity> spawnEvent = mock(Consumer.class);
            events.put("spawn", spawnEvent);

            Spawner spawner = SpawnerFactory.createFromConfig(config, events);

            assertInstanceOf(BonusSpawner.class, spawner);
            assertEquals(5f, ((BonusSpawner) spawner).getCooldown());
            verify(config, times(2)).getType();
            verify(config).getTexture();
            verify(config).getCooldown();
        }
    }

    @Test
    void createPlayerSpawnerReturnsConfiguredPlayerSpawnerWithEvents() {
        try (MockedStatic<Scaler> mocked = mockStatic(Scaler.class)) {
            mocked.when(Scaler::getInstance).thenReturn(mockScaler);
            when(mockScaler.getTileWidth()).thenReturn(32f);
            when(mockScaler.getTileHeight()).thenReturn(32f);

            when(config.getType()).thenReturn(SpawnerTypeEnum.PLAYER);
            when(config.getTexture()).thenReturn("spawner.png");
            when(config.getPlayerTexture()).thenReturn("player.png");
            Map<String, Consumer<Entity>> events = new HashMap<>();
            Consumer<Entity> spawnEvent = mock(Consumer.class);
            events.put("spawn", spawnEvent);

            Spawner spawner = SpawnerFactory.createFromConfig(config, events);

            assertInstanceOf(PlayerSpawner.class, spawner);
            assertEquals(events.get("spawn"), spawner.getEvent("spawn"));
            verify(config, times(2)).getType();
            verify(config).getTexture();
            verify(config).getPlayerTexture();
        }
    }

    @Test
    void createWithEmptyEventsThrowsNotConfiguredException() {
        Map<String, Consumer<Entity>> emptyEvents = new HashMap<>();

        assertThrows(
                NotConfiguredException.class,
                () -> SpawnerFactory.createFromConfig(config, emptyEvents)
        );
    }

    @Test
    void createFromConfigWithNullConfigThrowsIllegalArgumentException() {
        Map<String, Consumer<Entity>> events = new HashMap<>();

        assertThrows(
                NullPointerException.class,
                () -> SpawnerFactory.createFromConfig(null, events)
        );
        verifyNoInteractions(config);
    }

    @Test
    void createFromConfigWithNullSpawnerTypeThrowsIllegalArgumentException() {
        when(config.getType()).thenReturn(null);
        Map<String, Consumer<Entity>> events = new HashMap<>();
        events.put("someEvent", mock(Consumer.class));

        assertThrows(
                InvalidParameterException.class,
                () -> SpawnerFactory.createFromConfig(config, events)
        );
        verify(config).getType();
    }
}