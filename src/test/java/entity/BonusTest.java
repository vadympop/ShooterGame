package entity;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.effects.Effect;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.scene.graphics.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BonusTest {
    @Mock private Player mockPlayer;
    @Mock private Effect mockEffect;
    @Mock private Tile mockTile;
    @Mock private Bounds mockBounds;
    private Bonus bonus;

    @BeforeEach
    void setup() {
        bonus = new Bonus(mockEffect, mockTile, mockBounds);
    }

    @Test
    void testGetEffect() {
        assertEquals(mockEffect, bonus.getEffect());
    }

    @Test
    void testApplyEffectEffectApplied() {
        when(mockPlayer.applyEffect(mockEffect)).thenReturn(true);

        bonus.applyEffect(mockPlayer);

        assertFalse(bonus.getState());
        verify(mockPlayer).applyEffect(mockEffect);
    }

    @Test
    void testApplyEffectEffectNotApplied() {
        when(mockPlayer.applyEffect(mockEffect)).thenReturn(false);

        bonus.applyEffect(mockPlayer);

        assertTrue(bonus.getState());
        verify(mockPlayer).applyEffect(mockEffect);
    }

    @Test
    void testOnCollisionCallsVisitor() {
        CollisionVisitor visitor = mock(CollisionVisitor.class);
        Collidable other = mock(Collidable.class);

        bonus.onCollision(visitor, other);

        verify(visitor).visit(bonus, other);
    }

    @Test
    void testUpdateDoesNothing() {
        bonus.update(0.016);
    }
}
