package com.game.core.scene.spawners;

import com.game.core.behaviour.base.GameObject;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

public class PlayerSpawner extends GameObject implements Spawner {
    private Tile tile;

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

    @Override
    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
