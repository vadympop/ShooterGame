package com.game.core.entities;

import com.game.core.behaviour.base.CollidableGameObject;
import com.game.core.behaviour.interfaces.Renderable;
import com.game.core.behaviour.interfaces.Updatable;

public abstract class Entity extends CollidableGameObject implements Renderable, Updatable {
    private boolean isActive;
    private float speed;

    @Override
    public void draw() {

    }

    public void render() {
        if (getState()) draw();
    }

    public void setState(boolean active) {
        this.isActive = active;
    }

    public boolean getState() {
        return this.isActive;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
