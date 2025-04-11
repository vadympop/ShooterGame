package com.game.core.shooting;

import java.util.function.Consumer;

public class DelayedWrapper <T> {
    private double delay;
    private final T obj;
    private final Consumer<T> action;

    public DelayedWrapper(double delay, T obj, Consumer<T> action) {
        this.delay = delay;
        this.obj = obj;
        this.action = action;
    }

    public boolean canExecute(double deltaTime) {
        delay -= deltaTime;
        if (delay <= 0) {
            action.accept(obj);
            return true;
        }
        return false;
    }
}
