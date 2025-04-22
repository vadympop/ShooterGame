package utils;

import com.game.core.factories.AreaFactory;
import com.game.core.factories.BlockFactory;
import com.game.core.factories.SpawnerFactory;
import com.game.core.scene.areas.Area;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.graphics.TileType;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.GameSceneLoader;
import com.game.core.utils.QuatroConsumer;
import com.game.core.utils.Scaler;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.scenes.game.GameModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameSceneLoaderTest {
    @Mock private SceneConfig config;
    @Mock private Scaler scaler;
    @Mock private SceneConfig.MappingsConfig mappings;
    @Mock private SceneConfig.MappingTileConfig tileConfig;
    @Mock private SceneConfig.MappingBlockConfig blockConfig;
    @Mock private SceneConfig.AreaConfig areaConfig;
    @Mock private SceneConfig.SpawnerConfig spawnerConfig;
    @Mock private GameModel gameModel;
    @Mock private Area area;
    @Mock private Spawner spawner;
    @Mock private Block block;
    @Mock private BreakableBlock breakableBlock;

    private GameSceneLoader loader;
    private MockedStatic<Scaler> scalerMock;
    private MockedStatic<AreaFactory> areaFactoryMock;
    private MockedStatic<SpawnerFactory> spawnerFactoryMock;
    private MockedStatic<BlockFactory> blockFactoryMock;

    @BeforeEach
    void setup() {
        scalerMock = mockStatic(Scaler.class);
        areaFactoryMock = mockStatic(AreaFactory.class);
        spawnerFactoryMock = mockStatic(SpawnerFactory.class);
        blockFactoryMock = mockStatic(BlockFactory.class);

        scalerMock.when(Scaler::getInstance).thenReturn(scaler);
        loader = new GameSceneLoader(config, scaler);
    }

    @AfterEach
    void teardown() {
        scalerMock.close();
        areaFactoryMock.close();
        spawnerFactoryMock.close();
        blockFactoryMock.close();
    }

    @Test
    void constructorThrowsNullPointerExceptionForNullConfig() {
        assertThrows(
                NullPointerException.class,
                () -> new GameSceneLoader(null, scaler)
        );
    }

    @Test
    void constructorThrowsNullPointerExceptionForNullScaler() {
        assertThrows(
                NullPointerException.class,
                () -> new GameSceneLoader(config, null)
        );
    }

    @Test
    void loadSceneCreatesAndPopulatesGameModel() {
        when(config.getId()).thenReturn("test");
        when(config.getName()).thenReturn("Test Scene");
        List<String> backgroundTiles = List.of("---", "-A-");
        List<String> overlayTiles = List.of("---", "---");
        List<String> blocks = List.of("---", "-B-");
        when(config.getBackgroundTiles()).thenReturn(backgroundTiles);
        when(config.getOverlayTiles()).thenReturn(overlayTiles);
        when(config.getBlocks()).thenReturn(blocks);
        when(config.getAreas()).thenReturn(Collections.emptyList());
        when(config.getSpawners()).thenReturn(Collections.emptyList());
        when(config.getMappings()).thenReturn(mappings);
        when(mappings.getTiles()).thenReturn(Map.of("A", tileConfig));
        when(mappings.getBlocks()).thenReturn(Map.of("B", blockConfig));
        when(tileConfig.getTexture()).thenReturn("texture.png");
        when(tileConfig.isDefaultSize()).thenReturn(true);
        when(blockConfig.getTexture()).thenReturn("block.png");
        when(blockConfig.isDefaultSize()).thenReturn(true);

        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        blockFactoryMock.when(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class))).thenReturn(block);

        GameModel result = loader.loadScene();

        assertNotNull(result);
        assertEquals("test", result.getSceneId());
        assertEquals("Test Scene", result.getName());
        assertEquals(1, result.getTiles().size());
        assertEquals(1, result.getBlocks().size());
        assertEquals(0, result.getSpawners().size());
        assertEquals(0, result.getEntities().size());
        assertEquals(1, result.getTilesByType(TileType.BACKGROUND).size());
        assertEquals(0, result.getTilesByType(TileType.OVERLAY).size());
        verify(config).getId();
        verify(config).getName();
        verify(config).getBackgroundTiles();
        verify(config).getOverlayTiles();
        verify(config).getBlocks();
        verify(config).getAreas();
        verify(config).getSpawners();
        verify(mappings, times(2)).getTiles();
        verify(mappings).getBlocks();
        blockFactoryMock.verify(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class)));
    }

    @Test
    void generatePosCalculatesCorrectPosition() {
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        int colsCount = 2;
        int rowsCount = 3;

        float[] pos = loader.generatePos(colsCount, rowsCount);

        assertArrayEquals(new float[]{48f, 80f}, pos, 0.001f); // x = 2*32 - 32/2, y = 3*32 - 32/2
        verify(scaler, times(2)).getTileWidth();
        verify(scaler, times(2)).getTileHeight();
    }

    @Test
    void loadAreasAddsAreasToGameModel() {
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        when(areaConfig.getCol()).thenReturn(1);
        when(areaConfig.getRow()).thenReturn(1);
        when(config.getAreas()).thenReturn(List.of(areaConfig));
        areaFactoryMock.when(() -> AreaFactory.createFromConfig(areaConfig)).thenReturn(area);

        loader.loadAreas(gameModel);

        areaFactoryMock.verify(() -> AreaFactory.createFromConfig(areaConfig));
        verify(area).setPos(16f, 16f); // 1*32 - 32/2
        verify(gameModel).addArea(area);
    }

    @Test
    void loadAreasWithEmptyListDoesNothing() {
        when(config.getAreas()).thenReturn(Collections.emptyList());

        loader.loadAreas(gameModel);

        areaFactoryMock.verifyNoInteractions();
        verifyNoInteractions(gameModel);
    }

    @Test
    void loadSpawnersAddsSpawnersToGameModel() {
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        when(spawnerConfig.getCol()).thenReturn(2);
        when(spawnerConfig.getRow()).thenReturn(3);
        when(config.getSpawners()).thenReturn(List.of(spawnerConfig));
        spawnerFactoryMock.when(() -> SpawnerFactory.createFromConfig(eq(spawnerConfig), anyMap())).thenReturn(spawner);

        loader.loadSpawners(gameModel);

        spawnerFactoryMock.verify(() -> SpawnerFactory.createFromConfig(eq(spawnerConfig), anyMap()));
        verify(spawner).setPos(48f, 80f); // 2*32 - 32/2, 3*32 - 32/2
        verify(gameModel).addSpawner(spawner);
    }

    @Test
    void loadSpawnersWithEmptyListDoesNothing() {
        when(config.getSpawners()).thenReturn(Collections.emptyList());

        loader.loadSpawners(gameModel);

        spawnerFactoryMock.verifyNoInteractions();
        verifyNoInteractions(gameModel);
    }

    @Test
    void loadTilesAddsTilesToGameModel() {
        List<String> tiles = List.of("---", "-A-");
        when(mappings.getTiles()).thenReturn(Map.of("A", tileConfig));
        when(config.getMappings()).thenReturn(mappings);
        when(tileConfig.getTexture()).thenReturn("texture.png");
        when(tileConfig.isDefaultSize()).thenReturn(true);
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        loader.loadTiles(gameModel, tiles, TileType.BACKGROUND);

        verify(gameModel).addTile(any(SceneTile.class));
        verify(mappings).getTiles();
    }

    @Test
    void loadTilesWithEmptyListDoesNothing() {
        when(config.getMappings()).thenReturn(mappings);
        List<String> tiles = Collections.emptyList();

        loader.loadTiles(gameModel, tiles, TileType.BACKGROUND);

        verify(mappings).getTiles();
        verifyNoMoreInteractions(mappings);
        verifyNoInteractions(gameModel);
    }

    @Test
    void loadBlocksAddsBlocksToGameModel() {
        List<String> blocks = List.of("---", "-B-");
        when(config.getMappings()).thenReturn(mappings);
        when(mappings.getBlocks()).thenReturn(Map.of("B", blockConfig));
        when(blockConfig.getTexture()).thenReturn("block.png");
        when(blockConfig.isDefaultSize()).thenReturn(true);
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);
        blockFactoryMock.when(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class))).thenReturn(block);

        loader.loadBlocks(gameModel, blocks);

        blockFactoryMock.verify(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class)));
        verify(block).setPos(anyFloat(), anyFloat());
        verify(gameModel).addBlock(block);
        verify(mappings).getBlocks();
    }

    @Test
    void loadBlocksWithEntityAddsEntityToGameModel() {
        List<String> blocks = List.of("---", "-B-");
        when(mappings.getBlocks()).thenReturn(Map.of("B", blockConfig));
        when(config.getMappings()).thenReturn(mappings);
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        when(blockConfig.getTexture()).thenReturn("block.png");
        when(blockConfig.isDefaultSize()).thenReturn(true);
        blockFactoryMock
                .when(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class)))
                .thenReturn(breakableBlock);

        loader.loadBlocks(gameModel, blocks);

        blockFactoryMock.verify(() -> BlockFactory.createFromConfig(eq(blockConfig), any(Tile.class)));
        verify(breakableBlock).setPos(anyFloat(), anyFloat());
        verify(gameModel).addEntity(breakableBlock);
        verify(mappings).getBlocks();
    }

    @Test
    void loadBlocksWithEmptyListDoesNothing() {
        when(config.getMappings()).thenReturn(mappings);
        List<String> blocks = Collections.emptyList();

        loader.loadBlocks(gameModel, blocks);

        blockFactoryMock.verifyNoInteractions();
        verify(mappings).getBlocks();
        verifyNoMoreInteractions(mappings);
        verifyNoInteractions(gameModel);
    }

    @Test
    void loadObjectsFromStringsProcessesValidElements() {
        when(scaler.getTileWidth()).thenReturn(32f);
        when(scaler.getTileHeight()).thenReturn(32f);

        List<String> elements = List.of("---", "-A-");
        Map<String, SceneConfig.MappingTileConfig> texturesMapping = Map.of("A", tileConfig);
        when(tileConfig.getTexture()).thenReturn("texture.png");
        when(tileConfig.isDefaultSize()).thenReturn(true);
        QuatroConsumer<GameModel, SceneConfig.MappingTileConfig, Tile, float[]> addFunction =
                mock(QuatroConsumer.class);

        loader.loadObjectsFromStrings(gameModel, texturesMapping, elements, TileType.BACKGROUND, addFunction);

        verify(addFunction).accept(eq(gameModel), eq(tileConfig), any(Tile.class), any(float[].class));
    }

    @Test
    void loadObjectsFromStringsSkipsInvalidElements() {
        List<String> elements = List.of("---", "-X-");
        Map<String, SceneConfig.MappingTileConfig> texturesMapping = Map.of("A", tileConfig);
        QuatroConsumer<GameModel, SceneConfig.MappingTileConfig, Tile, float[]> addFunction =
                mock(QuatroConsumer.class);

        loader.loadObjectsFromStrings(gameModel, texturesMapping, elements, TileType.BACKGROUND, addFunction);

        verifyNoInteractions(addFunction);
    }

    @Test
    void loadObjectsFromStringsWithEmptyListDoesNothing() {
        List<String> elements = Collections.emptyList();
        Map<String, SceneConfig.MappingTileConfig> texturesMapping = Map.of();
        QuatroConsumer<GameModel, SceneConfig.MappingTileConfig, Tile, float[]> addFunction =
                mock(QuatroConsumer.class);

        loader.loadObjectsFromStrings(gameModel, texturesMapping, elements, TileType.BACKGROUND, addFunction);

        verifyNoInteractions(addFunction);
    }

    @Test
    void getConfigReturnsInjectedConfig() {
        SceneConfig result = loader.getConfig();
        assertSame(config, result);
    }

    @Test
    void getScalerReturnsInjectedScaler() {
        Scaler result = loader.getScaler();
        assertSame(scaler, result);
    }
}