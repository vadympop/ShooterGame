package com.game.core.utils;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.CircleBounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.scene.areas.Area;
import com.game.core.scene.areas.KillableArea;
import com.game.core.scene.areas.SlowingArea;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.config.SceneConfig;
import com.game.core.utils.config.enums.AreaTypeEnum;
import com.game.core.utils.config.enums.BoundsTypeEnum;
import com.game.core.utils.config.enums.SpawnerTypeEnum;
import com.game.gui.views.game.GameScene;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.graphics.TileType;

import java.util.List;
import java.util.Map;

public class SceneLoader {
    private SceneConfig config;
    private Scaler scaler;

    public SceneLoader(SceneConfig config, Scaler scaler) {
        setConfig(config);
        setScaler(scaler);
    }

    public GameScene loadScene() {
        SceneConfig config = getConfig();

        GameScene newScene = new GameScene(config.getId(), config.getName());
        loadTiles(newScene, config.getBackgroundTiles(), TileType.BACKGROUND);
        loadTiles(newScene, config.getOverlayTiles(), TileType.OVERLAY);
        loadBlocks(newScene, config.getBlocks());
        loadSpawners(newScene);
        loadAreas(newScene);

        return newScene;
    }

    public float[] generatePos(int colsCount, int rowsCount) {
        Scaler scaler = getScaler();

        float xPos = colsCount * scaler.getTileWidth() - (scaler.getTileWidth() / 2);
        float yPos = rowsCount * scaler.getTileHeight() - (scaler.getTileHeight() / 2);
        return new float[]{xPos, yPos};
    }

    public void loadAreas(GameScene scene) {
        getConfig().getAreas().forEach(x -> {
            Bounds bounds;
            if (x.getBounds().getType() == BoundsTypeEnum.RECTANGLE) {
                bounds = new RectangleBounds(x.getBounds().getWidth(), x.getBounds().getHeight());
            }
            else if (x.getBounds().getType() == BoundsTypeEnum.CIRCLE) {
                bounds = new CircleBounds(x.getBounds().getRadius(), x.getBounds().getRadius());
            } else return;

            Area area;
            if (x.getType() == AreaTypeEnum.KILLABLE) {
                area = new KillableArea(bounds);
            }
            else if (x.getType() == AreaTypeEnum.SLOWING) {
                area = new SlowingArea(bounds);
            }
            else return;

            float[] pos = generatePos(x.getCol(), x.getRow());
            area.setPos(pos[0], pos[1]);
            scene.addArea(area);
        });
    }

    public void loadSpawners(GameScene scene) {
        getConfig().getSpawners().forEach(x -> {
            Spawner spawner;
            if (x.getType() == SpawnerTypeEnum.PLAYER) {
                spawner = new PlayerSpawner(
                        new Tile(x.getTexture(), null),
                        new Tile(x.getPlayerTexture(), null)
                );
                spawner.addEvent("onPlayerCreated", scene::addEntity);
            }
            else if (x.getType() == SpawnerTypeEnum.BONUS) {
                spawner = new BonusSpawner(new Tile(x.getTexture(), null), x.getCooldown());
            }
            else return;

            float[] pos = generatePos(x.getCol(), x.getRow());
            spawner.setPos(pos[0], pos[1]);
            scene.addSpawner(spawner);
        });
    }

    public void loadTiles(GameScene scene, List<String> tiles, TileType tileType) {
        loadObjectsFromStrings(
                scene, getConfig().getMappings().getTiles(), tiles, tileType,
                (s, c, t, pos) -> {
                    SceneTile sceneTile = new SceneTile(t);
                    sceneTile.setPos(pos[0], pos[1]);
                    s.addTile(sceneTile);
                }
        );
    }

    public void loadBlocks(GameScene scene, List<String> blocks) {
        loadObjectsFromStrings(
                scene, getConfig().getMappings().getBlocks(), blocks, null,
                (s, c, t, pos) -> {
                    Block block = GameObjectsFactory.createBlock(c, t);

                    block.setPos(pos[0], pos[1]);
                    s.addBlock(block);
                }
        );
    }

    public <T extends SceneConfig.MappingTileConfig> void loadObjectsFromStrings(
            GameScene scene,
            Map<String, T> texturesMapping,
            List<String> elements,
            TileType tileType,
            QuatroConsumer<GameScene, T, Tile, float[]> addFunction
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
    private void setConfig(SceneConfig config) { this.config = config; }

    public Scaler getScaler() { return scaler; }
    private void setScaler(Scaler scaler) { this.scaler = scaler; }
}