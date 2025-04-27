package effects;

import com.game.core.effects.DoubleDamageEffect;
import com.game.core.entities.Player;
import com.game.core.entities.bullet.BulletType;
import com.game.core.shooting.ShootingManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DoubleDamageEffectTest {
    @Mock private Player player;
    @Mock private ShootingManager sm;

    @Test
    void apply_setsDoubleDamageBulletType() {
        when(player.getSm()).thenReturn(sm);

        new DoubleDamageEffect().apply(player);

        verify(sm).setBulletType(BulletType.DOUBLE_DAMAGED_STANDARD);
    }

    @Test
    void remove_setsStandardBulletType() {
        when(player.getSm()).thenReturn(sm);

        new DoubleDamageEffect().remove(player);

        verify(sm).setBulletType(BulletType.STANDARD);
    }

    @Test
    void getDuration_returnsCorrectValue() {
        DoubleDamageEffect effect = new DoubleDamageEffect();
        assertEquals(10f, effect.getDuration(), 0.001f);
    }
}
