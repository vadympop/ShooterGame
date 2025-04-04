package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.GameObjectsFactory;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PlayerSpawner extends GameObject implements Spawner {
    private Tile tile;
    private Tile playerTile;
    private final Map<String, Consumer<Entity>> events = new HashMap<>();
    private Player player = null;

    public PlayerSpawner(Tile tile, Tile playerTile) {
        setTile(tile);
        setPlayerTile(playerTile);
    }

    @Override
    public void spawn() {
        if (getPlayer() == null) {
            setPlayer(GameObjectsFactory.createPlayer(this));

            Consumer<Entity> event = getEvent("onPlayerCreated");
            if (event != null) event.accept(getPlayer());
        }

        getPlayer().respawn(getX(), getY());
    }

    @Override
    public void addEvent(String name, Consumer<Entity> event) {
        this.events.put(name, event);
    }

    @Override
    public Consumer<Entity> getEvent(String name) {
        return this.events.get(name);
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Tile getPlayerTile() { return playerTile; }
    public void setPlayerTile(Tile playerTile) { this.playerTile = playerTile; }
}
