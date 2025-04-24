package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.factories.PlayerFactory;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PlayerSpawner extends GameObject implements Spawner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerSpawner.class);

    private final Tile tile;
    private final Tile playerTile;
    private final Map<String, Consumer<Entity>> events = new HashMap<>();
    private Player player = null;

    public PlayerSpawner(Tile tile, Tile playerTile) {
        this.tile = Objects.requireNonNull(tile);
        this.playerTile = Objects.requireNonNull(playerTile);
    }

    @Override
    public void spawn() {
        if (getPlayer() == null) {
            LOGGER.info("PlayerSpawner's first player spawn");
            setPlayer(PlayerFactory.create(this));
            getPlayer().getSm().setOnBulletCreated(getEvent("onEntityCreated"));

            Consumer<Entity> event = getEvent("onEntityCreated");
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
    public Tile getPlayerTile() { return playerTile; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public int getPlayerKillsCount() { return getPlayer().getKillsCount(); }
}
