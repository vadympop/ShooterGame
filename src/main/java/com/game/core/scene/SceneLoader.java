package com.game.core.scene;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.core.scene.blocks.SolidBlock;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.Tile;
import com.game.core.scene.graphics.TileType;
import com.game.core.utils.QuatroConsumer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SceneLoader {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String pathToScenes = "scenes/";

    public Scene loadScene(String sceneId) throws IOException {
        File file = new File(this.pathToScenes + sceneId + "_scene.json");
        SceneConfig config = mapper.readValue(file, SceneConfig.class);

        Scene newScene = new Scene(sceneId, config.getName());
        loadTiles(newScene, config, config.getBackgroundTiles(), TileType.BACKGROUND);
        loadTiles(newScene, config, config.getOverlayTiles(), TileType.OVERLAY);
        loadBlocks(newScene, config, config.getBlocks());

        return newScene;
    }

    public void loadTiles(Scene scene, SceneConfig config, List<String> tiles, TileType tileType) {
        loadObjectsFromStrings(scene, config, tiles, tileType, (s, t, x, y) -> {
            SceneTile sceneTile = new SceneTile(t);
            sceneTile.setPos(x, y);
            s.addTile(sceneTile);
        });
    }

    public void loadBlocks(Scene scene, SceneConfig config, List<String> blocks) {
        loadObjectsFromStrings(scene, config, blocks, null, (s, t, x, y) -> {
            SolidBlock block = new SolidBlock(t);
            block.setPos(x, y);
            s.addCollidableGameObject(block);
        });
    }

    public void loadObjectsFromStrings(
            Scene scene,
            SceneConfig config,
            List<String> elements,
            TileType tileType,
            QuatroConsumer<Scene, Tile, Float, Float> addFunction
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
                        config.getMappings().getTiles().get(elementStr),
                        tileType,
                        config.getScale()
                );
                float xPos = colsCount * config.getTileWidth();
                float yPos = rowsCount * config.getTileHeight();
                addFunction.accept(scene, tile, xPos, yPos);
            }
        }
    }

    public static class SceneConfig {
        private String id;
        private float scale;
        private float tileWidth;
        private float tileHeight;
        private String name;
        private List<Area> areas;
        private List<Spawner> spawners;
        private List<String> backgroundTiles;
        private List<String> blocks;
        private List<String> overlayTiles;
        private Mappings mappings;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<Area> getAreas() { return areas; }
        public void setAreas(List<Area> areas) { this.areas = areas; }

        public List<Spawner> getSpawners() { return spawners; }
        public void setSpawners(List<Spawner> spawners) { this.spawners = spawners; }

        public List<String> getBackgroundTiles() { return backgroundTiles; }
        public void setBackgroundTiles(List<String> backgroundTiles) { this.backgroundTiles = backgroundTiles; }

        public List<String> getBlocks() { return blocks; }
        public void setBlocks(List<String> blocks) { this.blocks = blocks; }

        public List<String> getOverlayTiles() { return overlayTiles; }
        public void setOverlayTiles(List<String> overlayTiles) { this.overlayTiles = overlayTiles; }

        public Mappings getMappings() { return mappings; }
        public void setMappings(Mappings mappings) { this.mappings = mappings; }

        public float getScale() { return scale; }
        public void setScale(float scale) { this.scale = scale; }

        public float getTileWidth() { return tileWidth; }
        public void setTileWidth(float tileWidth) { this.tileWidth = tileWidth; }

        public float getTileHeight() { return tileHeight; }
        public void setTileHeight(float tileHeight) { this.tileHeight = tileHeight; }
    }

    public static class Area {
        private String type;
        private String col;
        private String row;
        private Bounds bounds;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getCol() { return col; }
        public void setCol(String col) { this.col = col; }

        public String getRow() { return row; }
        public void setRow(String row) { this.row = row; }

        public Bounds getBounds() { return bounds; }
        public void setBounds(Bounds bounds) { this.bounds = bounds; }
    }

    public static class Bounds {
        private String type;
        private String width;
        private String height;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getWidth() { return width; }
        public void setWidth(String width) { this.width = width; }

        public String getHeight() { return height; }
        public void setHeight(String height) { this.height = height; }
    }

    public static class Spawner {
        private String type;
        private String col;
        private String row;
        private String cooldown;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getCol() { return col; }
        public void setCol(String col) { this.col = col; }

        public String getRow() { return row; }
        public void setRow(String row) { this.row = row; }

        public String getCooldown() { return cooldown; }
        public void setCooldown(String cooldown) { this.cooldown = cooldown; }
    }

    public static class Mappings {
        private Map<String, String> tiles;
        private Map<String, String> spawners;
        private Map<String, String> blocks;

        public Map<String, String> getTiles() { return tiles; }
        public void setTiles(Map<String, String> tiles) { this.tiles = tiles; }

        public Map<String, String> getSpawners() { return spawners; }
        public void setSpawners(Map<String, String> spawners) { this.spawners = spawners; }

        public Map<String, String> getBlocks() { return blocks; }
        public void setBlocks(Map<String, String> blocks) { this.blocks = blocks; }
    }
}