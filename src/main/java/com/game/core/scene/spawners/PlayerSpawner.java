package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class PlayerSpawner extends GameObject implements Spawner {
    private Tile tile;

    public PlayerSpawner(Tile tile) {
        setTile(tile);
    }

    @Override
    public void spawn() {

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
}
