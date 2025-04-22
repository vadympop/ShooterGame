package effects;

import com.game.core.effects.TakeDamageEffect;
import com.game.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TakeDamageEffectTest {
    @Mock private Player player;

    @Test
    void apply_takesDamageIfHealthGreaterThanOne() {
        when(player.getHealth()).thenReturn(5);

        new TakeDamageEffect().apply(player);

        verify(player).takeDamage(1);
    }

    @Test
    void apply_doesNotTakeDamageIfHealthIsOne() {
        when(player.getHealth()).thenReturn(1);

        new TakeDamageEffect().apply(player);

        verify(player, never()).takeDamage(anyInt());
    }

    @Test
    void remove_doesNothing() {
        new TakeDamageEffect().remove(player);
        verifyNoInteractions(player);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        TakeDamageEffect effect = new TakeDamageEffect();
        assertEquals(20f, effect.getDuration(), 0.001f);
    }
}
