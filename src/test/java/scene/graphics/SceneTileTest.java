package scene.graphics;

import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SceneTileTest {
    @Mock private Tile mockTile;
    @Mock private GraphicsContext mockGC;
    private SceneTile sceneTile;

    @BeforeEach
    void setup() {
        sceneTile = new SceneTile(mockTile);
        sceneTile.setPos(100f, 150f);
    }

    @Test
    void testConstructor_shouldThrowIfTileIsNull() {
        assertThrows(NullPointerException.class, () -> new SceneTile(null));
    }

    @Test
    void testGetTile_shouldReturnProvidedTile() {
        assertEquals(mockTile, sceneTile.getTile());
    }

    @Test
    void testDraw_shouldDelegateToTileDraw() {
        sceneTile.draw(mockGC);

        verify(mockTile).draw(mockGC, 100f, 150f);
    }
}
