package com.game.core.controllers;

import com.game.core.behaviour.interfaces.Collidable;
import com.game.core.entities.Bullet;
import com.game.core.entities.Player;
import com.game.core.entities.bonus.Bonus;
import com.game.core.scene.blocks.BreakableBlock;
import com.game.core.scene.blocks.SolidBlock;

public interface CollisionVisitor {
    void visit(Player player, Collidable other);
    void visit(Bullet bullet, Collidable other);
    void visit(Bonus bonus, Collidable other);
    void visit(SolidBlock block, Collidable other);
    void visit(BreakableBlock block, Collidable other);
}
