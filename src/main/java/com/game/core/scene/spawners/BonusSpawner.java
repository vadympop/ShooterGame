package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.entities.Entity;
import com.game.core.entities.bonus.Bonus;
import com.game.core.entities.bonus.BonusType;
import com.game.core.factories.BonusFactory;
import com.game.core.scene.graphics.Tile;
import com.game.core.utils.Timer;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The BonusSpawner class is responsible for spawning bonus entities in the game.
 * It manages the cooldown and utilizes events to handle interactions when bonuses are created.
 */
public class BonusSpawner extends GameObject implements Spawner {
    private final float cooldown;
    private final Map<String, Consumer<Entity>> events = new HashMap<>();
    private Timer<BonusSpawner> spawnTimer;
    private final Tile tile;
    private Bonus currentBonus;

    /**
     * Constructs a BonusSpawner with the specified tile and cooldown period.
     *
     * @param tile     The tile associated with this spawner.
     * @param cooldown The cooldown time (in seconds) before spawning the next bonus.
     */
    public BonusSpawner(Tile tile, float cooldown) {
        this.tile = Objects.requireNonNull(tile);
        this.cooldown = cooldown;

        createSpawnTimer();
    }

    /**
     * Spawns a new bonus entity if no active bonus exists or the current bonus has expired.
     * Generates a random bonus type and sets its position based on the spawner's location.
     */
    @Override
    public void spawn() {
        if (currentBonus != null && currentBonus.getState()) return;

        Bonus bonus = BonusFactory.create(BonusType.randomBonus());
        bonus.setPos(getX(), getY());
        currentBonus = bonus;

        Consumer<Entity> event = getEvent("onEntityCreated");
        if (event != null) event.accept(bonus);
    }

    /**
     * Adds a custom event with a specified name that can be triggered when conditions are met.
     *
     * @param name  The name of the event.
     * @param event The Consumer<Entity> action to execute for this event.
     */
    @Override
    public void addEvent(String name, Consumer<Entity> event) {
        this.events.put(name, event);
    }

    /**
     * Retrieves the event with the specified name, if it exists.
     *
     * @param name The name of the event to retrieve.
     * @return The Consumer<Entity> event associated with the given name, or null if not found.
     */
    @Override
    public Consumer<Entity> getEvent(String name) {
        return this.events.get(name);
    }

    /**
     * Draws the associated tile at the spawner's location on the provided graphics context.
     *
     * @param gc The GraphicsContext used to render the tile.
     */
    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public void update(double deltaTime) {
        getSpawnTimer().update(deltaTime, this, null);
    }

    public Timer<BonusSpawner> getSpawnTimer() {
        return this.spawnTimer;
    }

    /**
     * Initializes the spawn timer for this spawner.
     * The timer is configured to trigger the spawn action after the set cooldown period
     * and repeats its execution as long as the timer remains active.
     */
    private void createSpawnTimer() {
        this.spawnTimer = new Timer<>(getCooldown(), BonusSpawner::spawn, true);
    }

    public float getCooldown() { return cooldown; }

    @Override public Tile getTile() { return tile; }
}
