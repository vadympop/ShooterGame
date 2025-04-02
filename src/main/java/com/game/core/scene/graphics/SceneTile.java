package com.game.core.scene.graphics;

import com.game.core.behaviour.base.GameObject;
import com.game.core.behaviour.interfaces.Renderable;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;


public class SceneTile extends GameObject implements Renderable {
    private Tile tile;

    public SceneTile(Tile tile) {
        setTile(tile);
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }
}
