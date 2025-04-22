package utils;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.utils.PositionUtils;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PositionUtilsTest {
    @Test
    void shouldCalculateDisplayPosForRectangleBounds() {
        RectangleBounds mockRectangle = mock(RectangleBounds.class);
        when(mockRectangle.getMaxSize()).thenReturn(10f);
        when(mockRectangle.getWidth()).thenReturn(8f);
        when(mockRectangle.getHeight()).thenReturn(6f);

        double[] displayPos = PositionUtils.generateDisplayPos(10f, 20f, mockRectangle);

        assertEquals(6.0, displayPos[0], 0.001);
        assertEquals(17.0, displayPos[1], 0.001);
    }

    @Test
    void shouldCalculateDisplayPosForBoundsWithMaxSize() {
        Bounds mockBounds = mock(Bounds.class);
        when(mockBounds.getMaxSize()).thenReturn(10f);

        double[] displayPos = PositionUtils.generateDisplayPos(10f, 20f, mockBounds);

        assertEquals(5.0, displayPos[0], 0.001);
        assertEquals(15.0, displayPos[1], 0.001);
    }

    @Test
    void shouldReturnCorrectPosWhenSizeIsZero() {
        Bounds mockBounds = mock(Bounds.class);
        when(mockBounds.getMaxSize()).thenReturn(0f);

        double[] displayPos = PositionUtils.generateDisplayPos(10f, 20f, mockBounds);

        assertEquals(10.0, displayPos[0], 0.001);
        assertEquals(20.0, displayPos[1], 0.001);
    }
}
