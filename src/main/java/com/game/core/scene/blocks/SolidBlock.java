package com.game.core.scene.blocks;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.managers.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;

public class SolidBlock extends CollidableGameObject implements Block {
    private Tile tile;

    public SolidBlock(Tile tile) {
        setTile(tile);
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    private void setTile(Tile tile) {
        this.tile = tile;
    }
}
