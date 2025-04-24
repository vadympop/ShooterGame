package com.game.core.collisions;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.bullet.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollisionHandler implements CollisionVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollisionHandler.class);

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
