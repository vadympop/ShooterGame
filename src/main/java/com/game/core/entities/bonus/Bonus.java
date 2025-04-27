package com.game.core.entities.bonus;

import com.game.core.behaviour.bounds.Bounds;
import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.effects.Effect;
import com.game.core.entities.Entity;
import com.game.core.entities.Player;
import com.game.core.collisions.CollisionVisitor;
import com.game.core.scene.graphics.Tile;

/**
 * Represents a bonus entity in the game world. Bonuses provide an effect
 * that can be applied to a player to modify their state or abilities.
 * A bonus is depicted using a tile and has collision properties defined by a hitbox.
 */
public class Bonus extends Entity {
    private final Effect effect;

    /**
     * Constructs a new Bonus entity with the specified effect, tile, and hitbox.
     *
     * @param effect The effect that this bonus applies to a player.
     * @param tile   The graphical representation of the bonus.
     * @param hitbox The hitbox that defines the collision boundaries of the bonus.
     */
    public Bonus(Effect effect, Tile tile, Bounds hitbox) {
        super(tile, hitbox);
        this.effect = effect;
    }

    public Effect getEffect() { return this.effect; }

    /**
     * Applies the bonus's effect to the specified player. If the effect is successfully
     * applied, the bonus is deactivated.
     *
     * @param player The {@link Player} to whom the effect is applied.
     */
    public void applyEffect(Player player) {
        boolean isApplied = player.applyEffect(this.getEffect());
        if (isApplied) this.setState(false);
    }

    @Override
    public void update(double deltaTime) {

    }

    /**
     * Handles a collision between this bonus and another collidable object using the
     * provided collision visitor.
     *
     * @param visitor The {@link CollisionVisitor} handling the collision.
     * @param other   The other {@link Collidable} object involved in the collision.
     */
    @Override
    public void onCollision(CollisionVisitor visitor, Collidable other) {
        visitor.visit(this, other);
    }
}
