package effects;

import com.game.core.effects.SpeedBoostEffect;
import com.game.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SpeedBoostEffectTest {
    @Mock private Player player;

    @Test
    void apply_doublesPlayerSpeed() {
        when(player.getSpeed()).thenReturn(100f);

        new SpeedBoostEffect().apply(player);

        verify(player).setSpeed(200f);
    }

    @Test
    void remove_restoresDefaultSpeed() {
        when(player.getDefaultSpeed()).thenReturn(120f);

        new SpeedBoostEffect().remove(player);

        verify(player).setSpeed(120f);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        SpeedBoostEffect effect = new SpeedBoostEffect();
        assertEquals(5f, effect.getDuration(), 0.001f);
    }
}
