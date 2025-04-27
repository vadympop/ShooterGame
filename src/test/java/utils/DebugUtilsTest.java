package utils;

import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.DebugUtils;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
public class DebugUtilsTest {
    @Mock private GraphicsContext gc;
    @Mock private CircleBounds circleBounds;
    @Mock private RectangleBounds rectBounds;
    @Mock private ConfigManager configManager;
    @Mock private SceneConfig config;

    private MockedStatic<ConfigManager> configLoaderMock;
    private MockedStatic<DebugUtils> debugUtilsMock;

    @BeforeEach
    void setup() {
        configLoaderMock = mockStatic(ConfigManager.class);
        debugUtilsMock = mockStatic(DebugUtils.class, CALLS_REAL_METHODS);
        configLoaderMock.when(ConfigManager::getInstance).thenReturn(configManager);
    }

    @AfterEach
    void teardown() {
        configLoaderMock.close();
        debugUtilsMock.close();
    }

    @Test
    void drawHitboxWithCircleBoundsDrawsOval() {
        when(circleBounds.getX()).thenReturn(50f);
        when(circleBounds.getY()).thenReturn(50f);
        when(circleBounds.getRadius()).thenReturn(10f);

        DebugUtils.drawHitbox(gc, circleBounds);

        verify(gc).setStroke(Color.AQUAMARINE);
        verify(gc).setLineWidth(1.5);
        verify(gc).strokeOval(40.0, 40.0, 20.0, 20.0); // x - r, y - r, 2*r, 2*r
        verifyNoMoreInteractions(gc);
        verifyNoInteractions(configManager, config);
    }

    @Test
    void drawHitboxWithRectangleBoundsDrawsRect() {
        when(rectBounds.getX()).thenReturn(50f);
        when(rectBounds.getY()).thenReturn(50f);
        when(rectBounds.getWidth()).thenReturn(20f);
        when(rectBounds.getHeight()).thenReturn(30f);

        DebugUtils.drawHitbox(gc, rectBounds);

        verify(gc).setStroke(Color.AQUAMARINE);
        verify(gc).setLineWidth(1.5);
        verify(gc).strokeRect(40.0, 35.0, 20.0, 30.0); // x - w/2, y - h/2, w, h
        verifyNoMoreInteractions(gc);
        verifyNoInteractions(configManager, config);
    }

    @Test
    void drawHitboxWithNullBoundsDoesNotDraw() {
        DebugUtils.drawHitbox(gc, null);

        verify(gc).setStroke(Color.AQUAMARINE);
        verify(gc).setLineWidth(1.5);
        verifyNoMoreInteractions(gc);
        verifyNoInteractions(configManager, config);
    }

    @Test
    void drawHitboxWithNullGraphicsContextThrowsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> DebugUtils.drawHitbox(null, circleBounds)
        );
        verifyNoInteractions(circleBounds, configManager, config);
    }

    @Test
    void drawHitboxIfDebugWithDebugTrueDrawsHitbox() {
        when(circleBounds.getX()).thenReturn(50f);
        when(circleBounds.getY()).thenReturn(50f);
        when(circleBounds.getRadius()).thenReturn(10f);
        when(configManager.getConfig()).thenReturn(config);
        when(config.isDebug()).thenReturn(true);

        DebugUtils.drawHitboxIfDebug(gc, circleBounds);

        verify(gc).setStroke(Color.AQUAMARINE);
        verify(gc).setLineWidth(1.5);
        verify(gc).strokeOval(40.0, 40.0, 20.0, 20.0);
        verify(configManager).getConfig();
        verify(config).isDebug();
        verifyNoMoreInteractions(gc, configManager, config);
    }

    @Test
    void drawHitboxIfDebugWithDebugFalseDoesNotDraw() {
        when(configManager.getConfig()).thenReturn(config);
        when(config.isDebug()).thenReturn(false);

        DebugUtils.drawHitboxIfDebug(gc, circleBounds);

        verify(configManager).getConfig();
        verify(config).isDebug();
        verifyNoInteractions(gc, circleBounds);
    }

    @Test
    void drawHitboxIfDebugWithNullBoundsDoesNotDraw() {
        when(configManager.getConfig()).thenReturn(config);
        when(config.isDebug()).thenReturn(true);

        DebugUtils.drawHitboxIfDebug(gc, null);

        verify(configManager).getConfig();
        verify(config).isDebug();
        verify(gc).setStroke(Color.AQUAMARINE);
        verify(gc).setLineWidth(1.5);
        verifyNoMoreInteractions(gc);
    }

    @Test
    void drawHitboxIfDebugWithNullGraphicsContextThrowsNullPointerException() {
        when(configManager.getConfig()).thenReturn(config);
        when(config.isDebug()).thenReturn(true);

        assertThrows(
                NullPointerException.class,
                () -> DebugUtils.drawHitboxIfDebug(null, circleBounds)
        );
        verify(configManager).getConfig();
        verify(config).isDebug();
        verifyNoInteractions(circleBounds);
    }
}