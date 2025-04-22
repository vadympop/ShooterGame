package effects;

import com.game.core.effects.ShieldEffect;
import com.game.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ShieldEffectTest {
    @Mock private Player player;

    @Test
    void apply_setsShieldTrue() {
        new ShieldEffect().apply(player);

        verify(player).setHasShield(true);
    }

    @Test
    void remove_setsShieldFalse() {
        new ShieldEffect().remove(player);

        verify(player).setHasShield(false);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        ShieldEffect effect = new ShieldEffect();
        assertEquals(20f, effect.getDuration(), 0.001f);
    }
}
