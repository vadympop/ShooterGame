package com.game.core.entities;


import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;

public abstract class Entity extends CollidableGameObject implements Renderable, Updatable {
    private boolean isActive;

    @Override
    public void render() {

    }

    public void setState(boolean active) {
        this.isActive = active;
    }

    public boolean getState() {
        return this.isActive;
    }
}
