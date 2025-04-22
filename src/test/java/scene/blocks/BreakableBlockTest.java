package scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.graphics.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class BreakableBlockTest {
    @Mock private Tile mockTile;
    @Mock private Bounds mockBounds;
    private BreakableBlock block;

    @BeforeEach
    void setup() {
        block = new BreakableBlock(mockTile, mockBounds);
    }

    @Test
    void testInitialDurability() {
        assertEquals(10, block.getDurability());
    }

    @Test
    void testTakeDamageReducesDurability() {
        block.takeDamage(3);
        assertEquals(7, block.getDurability());
    }

    @Test
    void testTakeDamageDoesNotBreakBlockIfDurabilityRemains() {
        block.takeDamage(5);
        assertTrue(block.getState());
    }

    @Test
    void testTakeDamageBreaksBlock() {
        block.takeDamage(10);
        assertFalse(block.getState());
    }

    @Test
    void testMultipleHitsBreakBlock() {
        block.takeDamage(4);
        block.takeDamage(4);
        block.takeDamage(2);
        assertFalse(block.getState());
    }

    @Test
    void testOnCollisionCallsVisitor() {
        CollisionVisitor visitor = mock(CollisionVisitor.class);
        Collidable other = mock(Collidable.class);

        block.onCollision(visitor, other);
        verify(visitor).visit(block, other);
    }

    @Test
    void testUpdateDoesNothing() {
        block.update(0.016);
    }
}
