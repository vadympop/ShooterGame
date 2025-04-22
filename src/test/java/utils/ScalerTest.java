package utils;

import com.game.core.exceptions.InvalidParameterException;
import com.game.core.exceptions.NotConfiguredException;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.SceneConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ScalerTest {
    @Mock private Dimension screenSize;

    private Scaler scaler;
    private MockedStatic<Toolkit> toolkitMock;

    @BeforeEach
    void setUp() {
        scaler = Scaler.getInstance();
        toolkitMock = mockStatic(Toolkit.class);

        try {
            Field instanceField = Scaler.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
            scaler = Scaler.getInstance();
        } catch (Exception e) {
            fail("Failed to reset Scaler singleton");
        }
    }

    @AfterEach
    void teardown() {
        toolkitMock.close();
    }

    @Test
    void getInstanceReturnsSingleton() {
        Scaler instance1 = Scaler.getInstance();
        Scaler instance2 = Scaler.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void getScaleReturnsOneWhenNotConfigured() {
        assertTrue(scaler.isNotConfigured());
        assertEquals(1.0f, scaler.getScale());
    }

    @Test
    void getScaleCalculatesCorrectly() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float expectedScale = Math.min(
                1920.0f / (10 * 32.0f),
                1080.0f / (10 * 32.0f)
        );
        assertEquals(expectedScale, scaler.getScale());
    }

    @Test
    void setSettingsUpdatesFields() {
        scaler.setSettings(20, 15, 64.0f, 48.0f);
        assertEquals(20, scaler.getHorizontalTilesCount());
        assertEquals(15, scaler.getVerticalTilesCount());
        assertEquals(64.0f, scaler.getDefaultTileWidth());
        assertEquals(48.0f, scaler.getDefaultTileHeight());
        assertFalse(scaler.isNotConfigured());
    }

    @Test
    void setSettingsThrowsOnInvalidParams() {
        assertThrows(
                InvalidParameterException.class,
                () -> scaler.setSettings(0, 10, 32.0f, 32.0f)
        );
        assertThrows(
                InvalidParameterException.class,
                () -> scaler.setSettings(10, 0, 32.0f, 32.0f)
        );
        assertThrows(
                InvalidParameterException.class,
                () -> scaler.setSettings(10, 10, 0.0f, 32.0f)
        );
        assertThrows(
                InvalidParameterException.class,
                () -> scaler.setSettings(10, 10, 32.0f, 0.0f)
        );
    }

    @Test
    void setSettingsWithSceneConfig() {
        SceneConfig config = mock(SceneConfig.class);
        String[][] tiles = new String[15][20];
        List<String> aa = Arrays.stream(tiles)
                .map(x -> String.join("", Collections.nCopies(x.length, "a")))
                .toList();

        when(config.getBackgroundTiles()).thenReturn(aa);
        when(config.getTileWidth()).thenReturn(64.0f);
        when(config.getTileHeight()).thenReturn(48.0f);

        scaler.setSettings(config);
        assertEquals(20, scaler.getHorizontalTilesCount());
        assertEquals(15, scaler.getVerticalTilesCount());
        assertEquals(64.0f, scaler.getDefaultTileWidth());
        assertEquals(48.0f, scaler.getDefaultTileHeight());
    }

    @Test
    void scaleAppliesScaler() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float scale = scaler.getScale();
        assertEquals(100.0f * scale, scaler.scale(100.0f));
    }

    @Test
    void getSceneWidthThrowsWhenNotConfigured() {
        assertThrows(NotConfiguredException.class, () -> scaler.getSceneWidth());
    }

    @Test
    void getSceneHeightThrowsWhenNotConfigured() {
        assertThrows(NotConfiguredException.class, () -> scaler.getSceneHeight());
    }

    @Test
    void getTileWidthThrowsWhenNotConfigured() {
        assertThrows(NotConfiguredException.class, () -> scaler.getTileWidth());
    }

    @Test
    void getTileHeightThrowsWhenNotConfigured() {
        assertThrows(NotConfiguredException.class, () -> scaler.getTileHeight());
    }

    @Test
    void getSceneWidthCalculatesCorrectly() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float scale = scaler.getScale();
        assertEquals(10 * 32.0f * scale, scaler.getSceneWidth());
    }

    @Test
    void getSceneHeightCalculatesCorrectly() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float scale = scaler.getScale();
        assertEquals(10 * 32.0f * scale, scaler.getSceneHeight());
    }

    @Test
    void getTileWidthCalculatesCorrectly() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float scale = scaler.getScale();
        assertEquals(32.0f * scale, scaler.getTileWidth());
    }

    @Test
    void getTileHeightCalculatesCorrectly() {
        scaler.setSettings(10, 10, 32.0f, 32.0f);
        when(screenSize.getWidth()).thenReturn(1920.0);
        when(screenSize.getHeight()).thenReturn(1080.0);
        toolkitMock.when(Toolkit::getDefaultToolkit).thenReturn(mock(Toolkit.class));
        when(Toolkit.getDefaultToolkit().getScreenSize()).thenReturn(screenSize);

        float scale = scaler.getScale();
        assertEquals(32.0f * scale, scaler.getTileHeight());
    }
}