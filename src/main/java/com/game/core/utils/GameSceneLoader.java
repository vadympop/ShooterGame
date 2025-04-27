package com.game.core.utils;

import com.game.core.entities.Entity;
import com.game.core.factories.AreaFactory;
import com.game.core.factories.BlockFactory;
import com.game.core.factories.SpawnerFactory;
import com.game.core.scene.areas.Area;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.scenes.game.GameModel;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.graphics.TileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Responsible for loading and initializing game scenes from a configuration.
 * This includes the setup of tiles, blocks, spawners, and areas in the game model.
 */
public class GameSceneLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSceneLoader.class);

    private final SceneConfig config;
    private final Scaler scaler;

    /**
     * Creates a new instance of GameSceneLoader.
     *
     * @param config the scene configuration that contains details about the game scene
     * @param scaler the scaler used for calculating positional transformations
     */
    public GameSceneLoader(SceneConfig config, Scaler scaler) {
        this.config = Objects.requireNonNull(config);
        this.scaler = Objects.requireNonNull(scaler);
    }

    /**
     * Loads and initializes the game scene based on the configuration.
     *
     * @return a fully constructed {@link GameModel} containing the game scene
     */
    public GameModel loadScene() {
        LOGGER.debug("Load game scene from a config");
        SceneConfig config = getConfig();

        GameModel newScene = new GameModel(config.getId(), config.getName(), config.getGameDuration());
        loadTiles(newScene, config.getBackgroundTiles(), TileType.BACKGROUND);
        loadTiles(newScene, config.getOverlayTiles(), TileType.OVERLAY);
        loadBlocks(newScene, config.getBlocks());
        loadSpawners(newScene);
        loadAreas(newScene);

        return newScene;
    }

    /**
     * Generates the position of an element based on its column and row.
     *
     * @param colsCount the column position of the element
     * @param rowsCount the row position of the element
     * @return a 2-element float array containing the x and y coordinates
     */
    public float[] generatePos(int colsCount, int rowsCount) {
        Scaler scaler = getScaler();

        float xPos = colsCount * scaler.getTileWidth() - (scaler.getTileWidth() / 2);
        float yPos = rowsCount * scaler.getTileHeight() - (scaler.getTileHeight() / 2);
        return new float[]{xPos, yPos};
    }

    /**
     * Loads areas into the GameModel based on the configuration.
     *
     * @param scene the {@link GameModel} to which the areas will be added
     */
    public void loadAreas(GameModel scene) {
        getConfig().getAreas().forEach(x -> {
            Area area = AreaFactory.createFromConfig(x);
            float[] pos = generatePos(x.getCol(), x.getRow());
            area.setPos(pos[0], pos[1]);
            scene.addArea(area);

            LOGGER.debug("Add area {} with coords x={}, y={}", area.getClass().getSimpleName(), pos[0], pos[1]);
        });
    }

    /**
     * Loads spawners into the GameModel based on the configuration.
     *
     * @param scene the {@link GameModel} to which the spawners will be added
     */
    public void loadSpawners(GameModel scene) {
        getConfig().getSpawners().forEach(x -> {
            Map<String, Consumer<Entity>> events = new HashMap<>();
            events.put("onEntityCreated", scene::addEntity);

            Spawner spawner = SpawnerFactory.createFromConfig(x, events);
            float[] pos = generatePos(x.getCol(), x.getRow());
            spawner.setPos(pos[0], pos[1]);
            scene.addSpawner(spawner);

            LOGGER.debug("Add spawner {} with coords x={}, y={}", spawner.getClass().getSimpleName(), pos[0], pos[1]);
        });
    }

    /**
     * Loads tiles into the GameModel based on the configuration.
     *
     * @param scene    the {@link GameModel} to which the tiles will be added
     * @param tiles    the list of tile configurations represented as strings
     * @param tileType the type of the tiles (e.g., BACKGROUND or OVERLAY)
     */
    public void loadTiles(GameModel scene, List<String> tiles, TileType tileType) {
        loadObjectsFromStrings(
                scene, getConfig().getMappings().getTiles(), tiles, tileType,
                (s, c, t, pos) -> {
                    SceneTile sceneTile = new SceneTile(t);
                    sceneTile.setPos(pos[0], pos[1]);
                    s.addTile(sceneTile);
                }
        );
    }

    /**
     * Loads blocks into the GameModel based on the configuration.
     *
     * @param scene  the {@link GameModel} to which the blocks will be added
     * @param blocks the list of block configurations represented as strings
     */
    public void loadBlocks(GameModel scene, List<String> blocks) {
        loadObjectsFromStrings(
                scene, getConfig().getMappings().getBlocks(), blocks, null,
                (s, c, t, pos) -> {
                    Block block = BlockFactory.createFromConfig(c, t);

                    block.setPos(pos[0], pos[1]);
                    if (block instanceof Entity e) s.addEntity(e);
                    else s.addBlock(block);
                }
        );
    }

    /**
     * Loads dynamic objects, such as tiles or blocks, into the GameModel by parsing configuration data.
     *
     * @param scene           the {@link GameModel} to which the objects will be added
     * @param texturesMapping a mapping of configuration strings to texture properties
     * @param elements        the list of configuration strings representing game elements
     * @param tileType        the type of the tile, null if not applicable
     * @param addFunction     a function that defines how to add the parsed object to the GameModel
     * @param <T>             a type parameter extending {@link SceneConfig.MappingTileConfig}
     */
    public <T extends SceneConfig.MappingTileConfig> void loadObjectsFromStrings(
            GameModel scene,
            Map<String, T> texturesMapping,
            List<String> elements,
            TileType tileType,
            QuatroConsumer<GameModel, T, Tile, float[]> addFunction
    ) {
        int rowsCount = 0;
        for (String row : elements) {
            rowsCount++;
            int colsCount = 0;
            for (char x : row.toCharArray()) {
                colsCount++;
                if (x == '-') continue;

                String elementStr = String.valueOf(x);
                T mappingElement = texturesMapping.get(elementStr);
                if (mappingElement == null) continue;

                Tile tile = new Tile(
                        mappingElement.getTexture(),
                        tileType,
                        null,
                        mappingElement.isDefaultSize()
                );
                float[] pos = generatePos(colsCount, rowsCount);
                addFunction.accept(scene, mappingElement, tile, pos);
            }
        }
    }

    public SceneConfig getConfig() { return config; }
    public Scaler getScaler() { return scaler; }
}