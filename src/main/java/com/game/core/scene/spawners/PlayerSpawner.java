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

/**
 * A spawner responsible for creating and managing a Player in the game.
 * This class also manages tile rendering, events, and player respawning.
 */
public class PlayerSpawner extends GameObject implements Spawner {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerSpawner.class);

    private final Tile tile;
    private final Tile playerTile;
    private final Map<String, Consumer<Entity>> events = new HashMap<>();
    private Player player = null;

    /**
     * Constructs a new PlayerSpawner with specified tiles for spawning and rendering the Player.
     *
     * @param tile       the tile representing the general spawning location.
     * @param playerTile the tile associated with the Player character.
     * @throws NullPointerException if either tile is null.
     */
    public PlayerSpawner(Tile tile, Tile playerTile) {
        this.tile = Objects.requireNonNull(tile);
        this.playerTile = Objects.requireNonNull(playerTile);
    }

    /**
     * Spawns the Player in the game.
     * If the Player does not already exist, it creates one and invokes the "onEntityCreated" event.
     * Otherwise, it respawns the Player at the specified location.
     */
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

    /**
     * Registers an event with a specified name and its corresponding callback.
     *
     * @param name  the name of the event.
     * @param event the callback to execute when the event is triggered.
     */
    @Override
    public void addEvent(String name, Consumer<Entity> event) {
        this.events.put(name, event);
    }

    /**
     * Retrieves a previously registered event by its name.
     *
     * @param name the name of the event to retrieve.
     * @return the corresponding event callback, or {@code null} if no event is registered under the specified name.
     */
    @Override
    public Consumer<Entity> getEvent(String name) {
        return this.events.get(name);
    }

    /**
     * Renders the spawner's tile at its designated coordinates on the given graphics context.
     *
     * @param gc the graphics context to draw on.
     */
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
