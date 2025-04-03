package com.game.core.managers;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;

public class CollisionHandler implements CollisionVisitor {
    @Override
    public void visit(Player player, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override
            public void visit(Player player, Collidable other) {

            }

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override
            public void visit(Bonus bonus, Collidable other) {

            }

            @Override
            public void visit(SolidBlock block, Collidable other) {

            }

            @Override
            public void visit(BreakableBlock block, Collidable other) {

            }
        }, player);
    }

    @Override
    public void visit(Bullet bullet, Collidable other) {
        other.onCollision(new CollisionVisitor() {
            @Override
            public void visit(Player player, Collidable other) {
                player.takeDamage(bullet.getDamage());
            }

            @Override
            public void visit(Bullet bullet, Collidable other) {
                bullet.setState(false);
            }

            @Override public void visit(Bonus bonus, Collidable other) {}
            @Override public void visit(SolidBlock block, Collidable other) {}

            @Override
            public void visit(BreakableBlock block, Collidable other) {
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
