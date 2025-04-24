package scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class SolidBlockTest {
    @Mock private Tile mockTile;
    @Mock private Bounds mockBounds;
    @Mock private GraphicsContext mockGc;
    private SolidBlock solidBlock;

    @BeforeEach
    void setup() {
        solidBlock = new SolidBlock(mockTile, mockBounds);
    }

    private void setupConfig() {
        SceneConfig mockConfig = mock(SceneConfig.class);
        when(mockConfig.isDebug()).thenReturn(false);

        ConfigManager loader = ConfigManager.getInstance();

        try {
            Field configField = ConfigManager.class.getDeclaredField("config");
            configField.setAccessible(true);
            configField.set(loader, mockConfig);
        } catch (Exception e) {
            fail("Unable to use reflection for set config field");
        }
    }

    @Test
    void testTileIsSetCorrectly() {
        assertEquals(mockTile, solidBlock.getTile());
    }

    @Test
    void testOnCollisionCallsVisitor() {
        CollisionVisitor visitor = mock(CollisionVisitor.class);
        Collidable other = mock(Collidable.class);

        solidBlock.onCollision(visitor, other);
        verify(visitor).visit(solidBlock, other);
    }

    @Test
    void testDrawDelegatesToTileAndDebugUtils(){
        setupConfig();

        solidBlock.setPos(100, 50);

        solidBlock.draw(mockGc);

        verify(mockTile).draw(mockGc, 100f, 50f);
    }
}
