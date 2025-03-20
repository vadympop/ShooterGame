package com.game.core.effects;

public class NoEffect implements Effect {
    private float duration;

    @Override
    public void apply() {

    }

    @Override
    public void remove() {

    }

    @Override
    public float getDuration() {
        return this.duration;
    }
}
