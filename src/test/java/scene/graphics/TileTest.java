package scene.graphics;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.graphics.TileType;
import com.game.core.utils.ResourceUtils;
import com.game.core.utils.Scaler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TileTest {
    @Mock private Scaler scaler;
    @Mock private Image sprite;
    @Mock private RectangleBounds size;
    @Mock private GraphicsContext graphicsContext;

    private MockedStatic<Scaler> scalerMock;
    private MockedStatic<ResourceUtils> resourceMock;

    @BeforeEach
    void setup() {
        scalerMock = mockStatic(Scaler.class);
        scalerMock.when(Scaler::getInstance).thenReturn(scaler);

        when(scaler.getTileWidth()).thenReturn(32.0f);
        when(scaler.getTileHeight()).thenReturn(32.0f);

        resourceMock = mockStatic(ResourceUtils.class);
    }

    @AfterEach
    void teardown() {
        scalerMock.close();
        resourceMock.close();
    }

    @Test
    void constructorWithTextureAndScaleSetsDefaults() {
        Tile tile = new Tile("test.png", 1.5f);
        assertEquals(TileType.OBJECT, tile.getType());
        assertEquals(1.5f, tile.getScale());
        assertNotNull(tile.getSize());
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }

    @Test
    void loadImageHandlesURISyntaxException() {
        // Mock resource to return a URL that throws URISyntaxException
        URL mockUrl = mock(URL.class);
        resourceMock.when(() -> ResourceUtils.getResource("/textures/test.png")).thenReturn(mockUrl);
        resourceMock.when(() -> ResourceUtils.getResource("/textures/undefined.png")).thenReturn(null);
        when(mockUrl.toString()).thenReturn("invalid://url");
        try {
            when(mockUrl.toURI()).thenThrow(new java.net.URISyntaxException("invalid://url", "Invalid URL"));
        } catch (java.net.URISyntaxException e) {
            fail("Failed to setup mock URL", e);
        }

        Tile tile = new Tile("test.png", TileType.BACKGROUND, 1.5f);
        assertTrue(tile.isTextureUndefined(), "Texture should be undefined on URISyntaxException");
        assertNull(tile.getSprite(), "Sprite should be null on URISyntaxException");
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }
    @Test
    void constructorWithNonDefaultSizeUsesSpriteSizes() {
        // Simulate valid texture load using reflection
        Tile tile = new Tile("test.png", TileType.OBJECT, 1.5f, false);
        try {
            Field spriteField = Tile.class.getDeclaredField("sprite");
            spriteField.setAccessible(true);
            spriteField.set(tile, sprite);
            Field undefinedField = Tile.class.getDeclaredField("isTextureUndefined");
            undefinedField.setAccessible(true);
            undefinedField.set(tile, false);
            Field sizeField = Tile.class.getDeclaredField("size");
            sizeField.setAccessible(true);
            sizeField.set(tile, size);
        } catch (Exception e) {
            fail("Failed to set fields via reflection", e);
        }

        when(size.getWidth()).thenReturn(50.0f);
        when(size.getHeight()).thenReturn(50.0f);

        assertFalse(tile.isTextureUndefined());
        assertNotNull(tile.getSprite());
        assertEquals(50.0f, tile.getSize().getWidth());
        assertEquals(50.0f, tile.getSize().getHeight());
    }

    @Test
    void constructorWithTextureTypeAndScaleSetsValues() {
        Tile tile = new Tile("test.png", TileType.BACKGROUND, 1.5f);
        assertEquals(TileType.BACKGROUND, tile.getType());
        assertEquals(1.5f, tile.getScale());
        assertNotNull(tile.getSize());
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }

    @Test
    void constructorWithNullScaleUsesScaler() {
        when(scaler.getScale()).thenReturn(2.0f);

        Tile tile = new Tile("test.png", TileType.OVERLAY, null);
        assertEquals(TileType.OVERLAY, tile.getType());
        assertEquals(2.0f, tile.getScale());
        assertNotNull(tile.getSize());
        verify(scaler).getScale();
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }

    @Test
    void constructorWithDefaultSizeUsesScalerSizes() {
        Tile tile = new Tile("test.png", TileType.OBJECT, 1.5f, true);
        assertNotNull(tile.getSize());
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }

    @Test
    void constructorWithNonDefaultSizeWhenTextureUndefinedUsesScalerSizes() {
        resourceMock.when(() -> ResourceUtils.getResource("/textures/test.png")).thenReturn(null);
        resourceMock.when(() -> ResourceUtils.getResource("/textures/undefined.png")).thenReturn(null);

        Tile tile = new Tile("test.png", TileType.BACKGROUND, 1.5f, false);
        assertTrue(tile.isTextureUndefined());
        assertNotNull(tile.getSize());
        verify(scaler).getTileWidth();
        verify(scaler).getTileHeight();
    }

    @Test
    void loadImageSetsUndefinedOnMissingTexture() {
        resourceMock.when(() -> ResourceUtils.getResource("/textures/test.png")).thenReturn(null);
        resourceMock.when(() -> ResourceUtils.getResource("/textures/undefined.png")).thenReturn(null);
        Tile tile = new Tile("test.png", TileType.OBJECT, 1.5f);
        assertTrue(tile.isTextureUndefined());
        assertNull(tile.getSprite());
    }

    @Test
    void drawWithUndefinedTextureFillsRect() {
        Tile tile = new Tile("test.png", TileType.OBJECT, 1.5f);
        // Use reflection to set isTextureUndefined to true
        try {
            Field field = Tile.class.getDeclaredField("isTextureUndefined");
            field.setAccessible(true);
            field.set(tile, true);
        } catch (Exception e) {
            fail("Unable to use reflection to set isTextureUndefined");
        }

        tile.draw(graphicsContext, 10.0f, 20.0f);
        verify(graphicsContext).fillRect(anyDouble(), anyDouble(), eq(32.0), eq(32.0));
    }

    @Test
    void drawWithValidTextureDrawsImage() {
        Tile tile = new Tile("test.png", TileType.OBJECT, 1.5f);
        // Use reflection to set sprite and isTextureUndefined
        try {
            Field spriteField = Tile.class.getDeclaredField("sprite");
            spriteField.setAccessible(true);
            spriteField.set(tile, sprite);

            Field undefinedField = Tile.class.getDeclaredField("isTextureUndefined");
            undefinedField.setAccessible(true);
            undefinedField.set(tile, false);
        } catch (Exception e) {
            fail("Failed to set sprite or isTextureUndefined");
        }

        tile.draw(graphicsContext, 10.0f, 20.0f);
        verify(graphicsContext).drawImage(eq(sprite), anyDouble(), anyDouble(), eq(32.0), eq(32.0));
    }

    @Test
    void gettersWorkCorrectly() {
        Tile tile = new Tile("test.png", TileType.OVERLAY, 1.5f);
        assertTrue(tile.getSprite() == null || tile.getSprite() instanceof Image);
        assertEquals(TileType.OVERLAY, tile.getType());
        assertEquals(1.5f, tile.getScale());
        assertNotNull(tile.getSize());
        assertTrue(tile.isTextureUndefined());
    }
}