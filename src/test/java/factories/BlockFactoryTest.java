package factories;

import com.game.core.factories.BlockFactory;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.config.SceneConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BlockFactoryTest {
    @Mock private SceneConfig.MappingBlockConfig config;
    @Mock private SceneConfig.BoundsConfig boundsConfig;
    @Mock private Tile tile;

    @BeforeEach
    void setup() {
        when(config.getHitbox()).thenReturn(boundsConfig);
        when(boundsConfig.getWidth()).thenReturn(10f);
        when(boundsConfig.getHeight()).thenReturn(10f);
    }

    @Test
    void testCreateFromConfig_shouldReturnBreakableBlock() {
        when(config.isBreakable()).thenReturn(true);

        Block block = BlockFactory.createFromConfig(config, tile);

        assertInstanceOf(BreakableBlock.class, block);
    }

    @Test
    void testCreateFromConfig_shouldReturnSolidBlock() {
        when(config.isBreakable()).thenReturn(false);

        Block block = BlockFactory.createFromConfig(config, tile);

        assertInstanceOf(SolidBlock.class, block);
    }
}
