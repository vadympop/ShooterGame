package com.game.core.scene.areas;

import com.game.core.behaviour.base.GameObject;
import com.game.core.behaviour.bounds.Bounds;
import com.game.core.effects.Effect;
import com.game.core.entities.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents an area within the game scene that can apply an effect to a player
 * and has defined boundaries.
 */
public abstract class Area extends GameObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(Area.class);

    private final Effect effect;
    private final Bounds bounds;

    /**
     * Constructs an Area with the specified effect and bounds.
     *
     * @param effect the effect to be associated with this area, applied to players inside the area.
     * @param bounds the boundaries of this area.
     * @throws NullPointerException if either the effect or bounds are null.
     */
    public Area(Effect effect, Bounds bounds) {
        this.effect = Objects.requireNonNull(effect);
        this.bounds = Objects.requireNonNull(bounds);
    }

    /**
     * Applies the area's effect to the specified player.
     *
     * @param player the player to apply the effect to.
     */
    public void applyEffect(Player player) {
        LOGGER.info("Area is applying effect to player, area={}, player={}", this.getClass().getSimpleName(), player);
        player.applyEffect(effect);
    }

    /**
     * Checks if the specified player is within the boundaries of this area.
     *
     * @param player the player to check.
     * @return {@code true} if the player is within the boundaries of this area, {@code false} otherwise.
     */
    public boolean contains(Player player) {
        return getBounds().contains(player.getHitbox());
    }

    /**
     * Updates the position of this area and its associated bounding box.
     *
     * @param x the new x-coordinate of the area.
     * @param y the new y-coordinate of the area.
     */
    @Override
    public void setPos(float x, float y) {
        super.setPos(x, y);
        Bounds b = getBounds();
        if (b != null) b.setPos(x, y);
    }

    public Bounds getBounds() { return bounds; }
}
