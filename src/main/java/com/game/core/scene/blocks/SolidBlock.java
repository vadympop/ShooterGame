package com.game.core.scene.blocks;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.bounds.RectangleBounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

public class SolidBlock extends CollidableGameObject implements Block {
    private Tile tile;

    public SolidBlock(Tile tile, Bounds hitbox) {
        super(hitbox);
        setTile(tile);
    }

    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }

    @Override
    public void draw(GraphicsContext gc) {
        getTile().draw(gc, getX(), getY());

        float width = ((RectangleBounds) getHitbox()).getWidth();
        float height = ((RectangleBounds) getHitbox()).getHeight();
        double displayX = getX() - (width / 2);
        double displayY = getY() - (height / 2);

        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeRect(displayX, displayY, width, height);
    }

    @Override public Tile getTile() { return tile; }
    private void setTile(Tile tile) { this.tile = Objects.requireNonNull(tile); }
}
