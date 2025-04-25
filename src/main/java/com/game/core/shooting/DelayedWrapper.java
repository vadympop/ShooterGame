package com.game.core.shooting;

import java.util.function.Consumer;

/**
 * A wrapper class that delays execution of an action on a given object.
 * The delay decreases over time until the action is executed.
 *
 * @param <T> The type of the object on which the action will be performed.
 */
public class DelayedWrapper<T> {
    private double delay;
    private final T obj;
    private final Consumer<T> action;

    /**
     * Constructs a {@code DelayedWrapper} with the specified delay, object, and action.
     *
     * @param delay  The initial delay before the action can be executed (in seconds).
     * @param obj    The object on which the action will be performed.
     * @param action The action to be performed on the object.
     */
    public DelayedWrapper(double delay, T obj, Consumer<T> action) {
        this.delay = delay;
        this.obj = obj;
        this.action = action;
    }

    /**
     * Checks if the action can be executed by decrementing the delay by the given time.
     * Executes the action if the delay reaches zero or below.
     *
     * @param deltaTime The time to subtract from the remaining delay (in seconds).
     * @return {@code true} if the action was executed, {@code false} otherwise.
     */
    public boolean canExecute(double deltaTime) {
        delay -= deltaTime;
        if (delay <= 0) {
            action.accept(obj);
            return true;
        }
        return false;
    }
}
