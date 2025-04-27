package effects;

import com.game.core.effects.ReduceSpeedEffect;
import com.game.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReduceSpeedEffectTest {
    @Mock private Player player;

    @Test
    void apply_reducesSpeedBy25Percent() {
        when(player.getSpeed()).thenReturn(100f);

        new ReduceSpeedEffect().apply(player);

        verify(player).setSpeed(50f);
    }

    @Test
    void remove_restoresDefaultSpeed() {
        when(player.getDefaultSpeed()).thenReturn(100f);

        new ReduceSpeedEffect().remove(player);

        verify(player).setSpeed(100f);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        ReduceSpeedEffect effect = new ReduceSpeedEffect();
        assertEquals(10f, effect.getDuration(), 0.001f);
    }
}
