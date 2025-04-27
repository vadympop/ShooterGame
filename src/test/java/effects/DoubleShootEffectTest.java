package effects;

import com.game.core.effects.DoubleShootEffect;
import com.game.core.entities.Player;
import com.game.core.shooting.ShootingManager;
import com.game.core.strategies.DoubleShootStrategy;
import com.game.core.strategies.SingleShootStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DoubleShootEffectTest {
    @Mock private Player player;
    @Mock private ShootingManager sm;

    @Test
    void apply_setsDoubleShootStrategy() {
        when(player.getSm()).thenReturn(sm);

        new DoubleShootEffect().apply(player);

        verify(sm).setShootingStrategy(isA(DoubleShootStrategy.class));
    }

    @Test
    void remove_setsSingleShootStrategy() {
        when(player.getSm()).thenReturn(sm);

        new DoubleShootEffect().remove(player);

        verify(sm).setShootingStrategy(isA(SingleShootStrategy.class));
    }

    @Test
    void getDuration_returnsCorrectValue() {
        DoubleShootEffect effect = new DoubleShootEffect();
        assertEquals(10f, effect.getDuration(), 0.001f);
    }
}
