package com.game.core.scene.areas;

import com.game.core.behaviour.base.GameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.Effect;
import com.game.core.entities.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Area extends GameObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(Area.class);

    private final Effect effect;
    private Bounds bounds;

    public Area(Effect effect) {
        this.effect = effect;
    }

    public void applyEffect(Player player) {
        LOGGER.info("Area is applying effect to player, area={}, player={}", this.getClass().getSimpleName(), player);
        player.applyEffect(effect);
    }
    public boolean contains(Player player) { return getBounds().contains(player.getHitbox()); }

    @Override
    public void setPos(float x, float y) {
        super.setPos(x, y);
        Bounds b = getBounds();
        if (b != null) b.setPos(x, y);
    }

    public Bounds getBounds() { return bounds; }
    protected void setBounds(Bounds bounds) { this.bounds = bounds; }
}
