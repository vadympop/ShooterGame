package com.game.core.scene.graphics;

import com.game.core.behaviour.base.GameObject;
import com.game.core.behaviour.interfaces.Renderable;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;


/**
 * Represents a scene tile in a game, which is a graphical element that can be rendered on the screen.
 * This class extends {@link GameObject} and implements {@link Renderable}.
 */
public class SceneTile extends GameObject implements Renderable {
    private final Tile tile;

    /**
     * Constructs a {@code SceneTile} with the specified tile.
     *
     * @param tile the {@link Tile} associated with this scene tile, must not be {@code null}
     * @throws NullPointerException if the {@code tile} is {@code null}
     */
    public SceneTile(Tile tile) {
        this.tile = Objects.requireNonNull(tile);
    }

    /**
     * Renders this {@code SceneTile} on the provided {@link GraphicsContext}, using the tile's texture and position.
     *
     * @param gc the {@link GraphicsContext} on which the tile will be drawn, must not be {@code null}
     */
    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public Tile getTile() { return tile; }
}
