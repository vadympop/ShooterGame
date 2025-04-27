package effects;

import com.game.core.effects.Effect;
import com.game.core.effects.NoEffect;
import com.game.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NoEffectTest {
    @Mock private Player player;

    @Test
    void apply_removesActiveEffectIfExists() {
        Effect activeEffect = mock(Effect.class);
        when(player.getActiveEffect()).thenReturn(activeEffect);

        new NoEffect().apply(player);

        verify(activeEffect).remove(player);
    }

    @Test
    void apply_doesNothingIfNoActiveEffect() {
        when(player.getActiveEffect()).thenReturn(null);

        new NoEffect().apply(player);

        verify(player, never()).getSm();
    }

    @Test
    void remove_doesNothing() {
        new NoEffect().remove(player);
        verifyNoInteractions(player);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        NoEffect effect = new NoEffect();
        assertEquals(600f, effect.getDuration(), 0.001f);
    }
}
