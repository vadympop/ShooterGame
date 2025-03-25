package com.game.core.behaviour.interfaces;

import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
    void draw(GraphicsContext gc);
    Tile getTile();
}
