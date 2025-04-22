package scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.Effect;
import com.game.core.entities.Player;
import com.game.core.scene.areas.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AreaTest {
    @Mock private Player mockPlayer;
    @Mock private Effect mockEffect;
    @Mock private Bounds mockBounds;
    private Area area;

    private static class TestArea extends Area {
        public TestArea(Effect effect, Bounds bounds) {
            super(effect, bounds);
        }
    }

    @BeforeEach
    void setup() {
        area = new TestArea(mockEffect, mockBounds);
    }

    @Test
    void testApplyEffect() {
        area.applyEffect(mockPlayer);
        verify(mockPlayer).applyEffect(mockEffect);
    }

    @Test
    void testContainsWhenBoundsContainsPlayer() {
        when(mockPlayer.getHitbox()).thenReturn(mock(Bounds.class));
        when(mockBounds.contains(any())).thenReturn(true);

        assertTrue(area.contains(mockPlayer));
    }

    @Test
    void testContainsWhenBoundsDoesNotContainPlayer() {
        when(mockPlayer.getHitbox()).thenReturn(mock(Bounds.class));
        when(mockBounds.contains(any())).thenReturn(false);

        assertFalse(area.contains(mockPlayer));
    }

    @Test
    void testSetPosAlsoUpdatesBounds() {
        area.setPos(5f, 10f);
        verify(mockBounds).setPos(5f, 10f);
    }

    @Test
    void testGetBounds() {
        assertEquals(mockBounds, area.getBounds());
    }
}
