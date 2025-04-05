package com.game.core.utils;

import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.scene.spawners.BonusSpawner;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.views.game.GameScene;
import com.game.core.scene.blocks.SolidBlock;
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

        });
    }

    public void loadSpawners(GameScene scene) {
        getConfig().getSpawners().forEach(x -> {
            Spawner spawner;
            if (x.getType() == 0) {
                spawner = new PlayerSpawner(
                        new Tile(x.getTexture(), null),
                        new Tile(x.getPlayerTexture(), null)
                );
                spawner.addEvent("onPlayerCreated", scene::addEntity);
            }
            else if (x.getType() == 1) spawner = new BonusSpawner(new Tile(x.getTexture(), null), x.getCooldown());
            else return;

            float[] pos = generatePos(x.getCol(), x.getRow());
            spawner.setPos(pos[0], pos[1]);
            scene.addSpawner(spawner);
        });
    }

    public void loadTiles(GameScene scene, List<String> tiles, TileType tileType) {
        loadObjectsFromStrings(scene, getConfig().getMappings().getTiles(), tiles, tileType, (s, t, x, y) -> {
            SceneTile sceneTile = new SceneTile(t);
            sceneTile.setPos(x, y);
            s.addTile(sceneTile);
        });
    }

    public void loadBlocks(GameScene scene, List<String> blocks) {
        loadObjectsFromStrings(scene, getConfig().getMappings().getBlocks(), blocks, null, (s, t, x, y) -> {
            SolidBlock block = new SolidBlock(t);
            block.setPos(x, y);
            block.setBounds(new RectangleBounds(32, 32));
            s.addBlock(block);
        });
    }

    public void loadObjectsFromStrings(
            GameScene scene,
            Map<String, String> texturesMapping,
            List<String> elements,
            TileType tileType,
            QuatroConsumer<GameScene, Tile, Float, Float> addFunction
    ) {
        int rowsCount = 0;
        for (String row : elements) {
            rowsCount++;
            int colsCount = 0;
            for (char x : row.toCharArray()) {
                colsCount++;
                if (x == '-') continue;
                String elementStr = String.valueOf(x);
                Tile tile = new Tile(
                        texturesMapping.get(elementStr),
                        tileType,
                        null
                );
                float[] pos = generatePos(colsCount, rowsCount);
                addFunction.accept(scene, tile, pos[0], pos[1]);
            }
        }
    }

    public SceneConfig getConfig() { return config; }
    private void setConfig(SceneConfig config) { this.config = config; }

    public Scaler getScaler() { return scaler; }
    private void setScaler(Scaler scaler) { this.scaler = scaler; }
}