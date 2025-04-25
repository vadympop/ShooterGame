package com.game.core.collisions;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles collisions between various types of game entities by implementing the
 * {@link CollisionVisitor} interface. The class provides logic for resolving
 * interactions when different objects collide in the game environment.
 */
public class CollisionHandler implements CollisionVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollisionHandler.class);

    /**
     * Resolves collisions where a {@link Player} is involved.
     *
     * @param player The player entity that was part of the collision.
     * @param other  The other collidable entity.
     */
    @Override
    public void visit(Player player, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override public void visit(Player player, Collidable other) {}

            @Override
            public void visit(Bullet bullet, Collidable other) {
                if(bullet.getOwner() == player) {
                    LOGGER.debug("Bullet collides with its owner");
                    return;
                }
                bullet.setState(false);
            }

            @Override
            public void visit(Bonus bonus, Collidable other) {
                bonus.applyEffect(player);
            }

            @Override public void visit(SolidBlock block, Collidable other) {}
            @Override public void visit(BreakableBlock block, Collidable other) {}
        }, player);
    }

    /**
     * Resolves collisions where a {@link Bullet} is involved.
     *
     * @param bullet The bullet entity that was part of the collision.
     * @param other  The other collidable entity.
     */
    @Override
    public void visit(Bullet bullet, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override
            public void visit(Player player, Collidable other) {
                if(bullet.getOwner() == player) {
                    LOGGER.debug("Bullet collides with its owner 2");
                    return;
                }

                bullet.setState(false);
                player.takeDamage(bullet.getDamage(), bullet.getOwner());
            }

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override
            public void visit(Bonus bonus, Collidable other) {
                bullet.setState(false);
            }

            @Override
            public void visit(SolidBlock block, Collidable other) {
                bullet.setState(false);
            }

            @Override
            public void visit(BreakableBlock block, Collidable other) {
                bullet.setState(false);
                block.takeDamage(2);
            }
        }, bullet);
    }

    /**
     * Resolves collisions where a {@link Bonus} is involved.
     *
     * @param bonus The bonus entity that was part of the collision.
     * @param other The other collidable entity.
     */
    @Override
    public void visit(Bonus bonus, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override
            public void visit(Player player, Collidable other) {
                bonus.applyEffect(player);
            }

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override public void visit(Bonus bonus, Collidable other) {}
            @Override public void visit(SolidBlock block, Collidable other) {}
            @Override public void visit(BreakableBlock block, Collidable other) {}
        }, bonus);
    }

    /**
     * Resolves collisions where a {@link SolidBlock} is involved.
     *
     * @param block The solid block entity that was part of the collision.
     * @param other The other collidable entity.
     */
    @Override
    public void visit(SolidBlock block, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override public void visit(Player player, Collidable other) {}

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override public void visit(Bonus bonus, Collidable other) {}
            @Override public void visit(SolidBlock block, Collidable other) {}
            @Override public void visit(BreakableBlock block, Collidable other) {}
        }, block);
    }

    /**
     * Resolves collisions where a {@link BreakableBlock} is involved.
     *
     * @param block The breakable block entity that was part of the collision.
     * @param other The other collidable entity.
     */
    @Override
    public void visit(BreakableBlock block, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override public void visit(Player player, Collidable other) {}

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override public void visit(Bonus bonus, Collidable other) {}
            @Override public void visit(SolidBlock block, Collidable other) {}
            @Override public void visit(BreakableBlock block, Collidable other) {}
        }, block);
    }
}
