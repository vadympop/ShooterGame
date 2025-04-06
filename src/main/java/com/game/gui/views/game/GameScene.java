package com.game.gui.views.game;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.entities.Entity;
import com.game.core.collisions.CollisionManager;
import com.game.core.scene.areas.Area;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.TileType;
import com.game.core.scene.spawners.Spawner;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class GameScene {
    private final List<CollidableGameObject> collidableGameObjects = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final List<Area> areas = new ArrayList<>();
    private final List<Spawner> spawners = new ArrayList<>();
    private final List<SceneTile> tiles = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager();
    private String sceneId;
    private String name;

    public GameScene(String sceneId, String name) {
        setSceneId(sceneId);
        setName(name);
    }

    private void drawTilesByType(GraphicsContext gc, TileType type) {
        tiles.stream()
                .filter(tile -> tile.getTile().getType() == type)
                .forEach(tile -> tile.draw(gc));
    }

    public void start() {
        spawners.forEach(Spawner::spawn);
    }

    public void render(GraphicsContext gc) {
        drawTilesByType(gc, TileType.BACKGROUND);

        Stream.of(spawners, blocks, entities).forEach(list -> list.forEach(x -> x.draw(gc)));

        drawTilesByType(gc, TileType.OVERLAY);
    }

    public void update(double deltaTime) {
        entities.forEach(x -> x.update(deltaTime));

        collisionManager.checkCollisions(this);
    }

    public void addCollidableGameObject(CollidableGameObject obj) {
        this.collidableGameObjects.add(obj);
    }

    public void addBlock(Block block) {
        addCollidableGameObject((CollidableGameObject) block);
        this.blocks.add(block);
    }

    public void addEntity(Entity obj) {
        addCollidableGameObject(obj);
        this.entities.add(obj);
    }

    public void addSpawner(Spawner spawner) { this.spawners.add(spawner); }
    public void addArea(Area area) { this.areas.add(area); }
    public void addTile(SceneTile tile) { this.tiles.add(tile); }

    public String getSceneId() { return sceneId; }
    private void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getName() { return name; }
    private void setName(String name) { this.name = name; }

    public List<CollidableGameObject> getCollidableGameObjects() { return Collections.unmodifiableList(collidableGameObjects); }
    public List<Entity> getEntities() { return Collections.unmodifiableList(entities); }
    public List<Area> getAreas() { return Collections.unmodifiableList(areas); }
    public List<Spawner> getSpawners() { return Collections.unmodifiableList(spawners); }
    public List<SceneTile> getTiles() { return Collections.unmodifiableList(tiles); }
    public List<Block> getBlocks() { return Collections.unmodifiableList(blocks); }
}
