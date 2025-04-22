package com.game.gui.scenes.game;

import com.game.core.entities.Entity;
import com.game.core.collisions.CollisionManager;
import com.game.core.scene.areas.Area;
import com.game.core.scene.blocks.Block;
import com.game.core.scene.graphics.SceneTile;
import com.game.core.scene.graphics.TileType;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameModel {
    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> entitiesToAdd = new ArrayList<>();

    private final List<Spawner> spawners = new ArrayList<>();
    private final List<SceneTile> tiles = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();
    private final CollisionManager collisionManager = new CollisionManager();
    private String sceneId;
    private String name;

    public GameModel(String sceneId, String name) {
        setSceneId(sceneId);
        setName(name);
    }

    public void update(double deltaTime) {
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();

        entities.forEach(x -> x.update(deltaTime));
        spawners.forEach(x -> x.update(deltaTime));

        List<Entity> toRemove = entities.stream().filter(x -> !x.getState()).toList();
        collisionManager.removeEntities(toRemove);
        entities.removeAll(toRemove);
    }

    public List<SceneTile> getTilesByType(TileType type) {
        return getTiles().stream()
                .filter(tile -> tile.getTile().getType() == type)
                .toList();
    }

    public List<PlayerSpawner> getPlayerSpawners() {
        return getSpawners().stream()
                .filter(x -> x instanceof PlayerSpawner)
                .map(x -> (PlayerSpawner) x)
                .toList();
    }

    public void addBlock(Block block) {
        collisionManager.addObject(block);
        this.blocks.add(block);
    }

    public void addEntity(Entity obj) {
        obj.setCm(collisionManager);
        collisionManager.addObject(obj);
        entitiesToAdd.add(obj);
    }

    public void addArea(Area area) { collisionManager.addArea(area); }
    public void addSpawner(Spawner spawner) {
        this.spawners.add(spawner);
    }
    public void addTile(SceneTile tile) { this.tiles.add(tile); }

    public String getSceneId() { return sceneId; }
    private void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public String getName() { return name; }
    private void setName(String name) { this.name = name; }

    public List<Entity> getEntities() { return Collections.unmodifiableList(entities); }
    public List<Spawner> getSpawners() { return Collections.unmodifiableList(spawners); }
    public List<SceneTile> getTiles() { return Collections.unmodifiableList(tiles); }
    public List<Block> getBlocks() { return Collections.unmodifiableList(blocks); }
}
