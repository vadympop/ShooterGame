package effects;

import com.game.core.effects.CircularShootEffect;
import com.game.core.entities.Player;
import com.game.core.shooting.ShootingManager;
import com.game.core.strategies.CircularShootStrategy;
import com.game.core.strategies.SingleShootStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CircularShootEffectTest {
    @Mock private Player player;
    @Mock private ShootingManager sm;

    @Test
    void apply_setsCircularShootStrategy() {
        when(player.getSm()).thenReturn(sm);

        new CircularShootEffect().apply(player);

        verify(sm).setShootingStrategy(isA(CircularShootStrategy.class));
    }

    @Test
    void remove_setsSingleShootStrategy() {
        when(player.getSm()).thenReturn(sm);

        new CircularShootEffect().remove(player);

        verify(sm).setShootingStrategy(isA(SingleShootStrategy.class));
    }

    @Test
    void getDuration_returnsCorrectValue() {
        CircularShootEffect effect = new CircularShootEffect();
        assertEquals(20f, effect.getDuration(), 0.001f);
    }
}
