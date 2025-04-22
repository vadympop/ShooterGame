package scene.areas;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.ReduceSpeedEffect;
import com.game.core.effects.TakeDamageEffect;
import com.game.core.entities.Player;
import com.game.core.scene.areas.KillableArea;
import com.game.core.scene.areas.SlowingArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SpecificAreaTest {
    @Mock private Bounds mockBounds;
    @Mock private Player mockPlayer;

    @BeforeEach
    public void setup() {
        mockBounds = mock(Bounds.class);
        mockPlayer = mock(Player.class);
    }

    @Test
    void testKillableAreaInitializationAndEffectApplication() {
        KillableArea killableArea = new KillableArea(mockBounds);

        killableArea.applyEffect(mockPlayer);
        verify(mockPlayer).applyEffect(isA(TakeDamageEffect.class));

        assertEquals(mockBounds, killableArea.getBounds());
    }

    @Test
    void testSlowingAreaInitializationAndEffectApplication() {
        SlowingArea slowingArea = new SlowingArea(mockBounds);

        slowingArea.applyEffect(mockPlayer);
        verify(mockPlayer).applyEffect(isA(ReduceSpeedEffect.class));

        assertEquals(mockBounds, slowingArea.getBounds());
    }

    @Test
    void testKillableAreaContainsPlayer() {
        when(mockPlayer.getHitbox()).thenReturn(mock(Bounds.class));
        when(mockBounds.contains(any())).thenReturn(true);

        KillableArea area = new KillableArea(mockBounds);
        assertTrue(area.contains(mockPlayer));
    }

    @Test
    void testSlowingAreaDoesNotContainPlayer() {
        when(mockPlayer.getHitbox()).thenReturn(mock(Bounds.class));
        when(mockBounds.contains(any())).thenReturn(false);

        SlowingArea area = new SlowingArea(mockBounds);
        assertFalse(area.contains(mockPlayer));
    }
}
