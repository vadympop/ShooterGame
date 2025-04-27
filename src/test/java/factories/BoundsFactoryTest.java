package factories;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.factories.BoundsFactory;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BoundsFactoryTest {
    @Mock private Scaler scaler;
    @Mock private SceneConfig.BoundsConfig config;

    private MockedStatic<Scaler> scalerMock;

    @BeforeEach
    void setup() {
        scalerMock = mockStatic(Scaler.class);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);
        when(scaler.getScale()).thenReturn(1f);
    }

    @AfterEach
    void teardown() {
        scalerMock.close();
    }

    @Test
    void testCreateFromConfig_shouldReturnCircleBounds() {
        when(config.getType()).thenReturn(BoundsTypeEnum.CIRCLE);
        when(config.getRadius()).thenReturn(5f);

        Bounds bounds = BoundsFactory.createFromConfig(config);

        assertInstanceOf(CircleBounds.class, bounds);
    }

    @Test
    void testCreateFromConfig_shouldReturnRectangleBounds() {
        when(config.getType()).thenReturn(BoundsTypeEnum.RECTANGLE);
        when(config.getWidth()).thenReturn(10f);
        when(config.getHeight()).thenReturn(20f);

        Bounds bounds = BoundsFactory.createFromConfig(config);

        assertInstanceOf(RectangleBounds.class, bounds);
    }

    @Test
    void testCreateForBlock_shouldReturnRectangleBounds_fromConfig() {
        when(config.getWidth()).thenReturn(15f);
        when(config.getHeight()).thenReturn(25f);

        RectangleBounds bounds = BoundsFactory.createForBlock(config);

        assertEquals(15f, bounds.getWidth(), 0.01);
        assertEquals(25f, bounds.getHeight(), 0.01);
    }

    @Test
    void testCreateForBlock_shouldReturnRectangleBounds_fromSceneDefault_whenConfigIsNull() {
        SceneConfig mockSceneConfig = mock(SceneConfig.class);
        when(mockSceneConfig.getTileWidth()).thenReturn(32f);
        when(mockSceneConfig.getTileHeight()).thenReturn(32f);

        ConfigManager loaderMock = mock(ConfigManager.class);
        when(loaderMock.getConfig()).thenReturn(mockSceneConfig);

        try (MockedStatic<ConfigManager> mockedLoader = mockStatic(ConfigManager.class)) {
            mockedLoader.when(ConfigManager::getInstance).thenReturn(loaderMock);

            RectangleBounds bounds = BoundsFactory.createForBlock(null);

            assertEquals(32f, bounds.getWidth(), 0.01);
            assertEquals(32f, bounds.getHeight(), 0.01);
        }
    }
}
