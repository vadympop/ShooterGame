package com.game.core.utils;

import java.util.function.Consumer;

/**
 * A utility class to manage timer-related functionality, with support for executing a
 * task after a specified time interval and optional repetitive execution.
 *
 * @param <T> the type of object the consumer function processes when executed
 */
public class Timer<T> {
    private double baseTime;
    private boolean isRepetitive = false;
    private double timeLeft;
    private Consumer<T> func;

    /**
     * Constructs a Timer with a specified time interval and a function
     * to execute when the timer ends.
     *
     * @param time the initial time interval in seconds
     * @param func the function to execute when the timer ends
     */
    public Timer(double time, Consumer<T> func) {
        this(time, func, false);
    }

    /**
     * Constructs a Timer with a specified time interval, a function
     * to execute, and a flag for repetitive execution.
     *
     * @param time         the initial time interval in seconds
     * @param func         the function to execute when the timer ends
     * @param isRepetitive specifies whether the timer should reset and
     *                     repeat execution upon completion
     */
    public Timer(double time, Consumer<T> func, boolean isRepetitive) {
        if (isRepetitive) setBaseTime(time);

        setRepetitive(isRepetitive);
        setTimeLeft(time);
        setFunc(func);
    }

    /**
     * Decreases the remaining time of the timer by the specified value.
     *
     * @param value the amount of time to subtract from the timer in seconds
     */
    public void decreaseTime(double value) {
        setTimeLeft(getTimeLeft() - value);
    }

    /**
     * Updates the timer with elapsed time, executing the specified function
     * when the timer ends. For repetitive timers, it resets and starts again.
     *
     * @param deltaTime the amount of time elapsed since the last update in seconds
     * @param obj       the object to pass to the consumer function
     * @param func      a runnable to execute if the timer is not repetitive and ends
     */
    public void update(double deltaTime, T obj, Runnable func) {
        decreaseTime(deltaTime);

        if (getTimeLeft() <= 0) {
            getFunc().accept(obj);

            if (isRepetitive()) {
                setTimeLeft(getBaseTime());
            } else {
                if (func != null) func.run();
            }
        }
    }

    public double getTimeLeft() { return timeLeft; }
    public void setTimeLeft(double timeLeft) { this.timeLeft = timeLeft; }

    public Consumer<T> getFunc() { return func; }
    private void setFunc(Consumer<T> func) { this.func = func; }

    public double getBaseTime() { return baseTime; }
    private void setBaseTime(double baseTime) { this.baseTime = baseTime; }

    public boolean isRepetitive() { return this.isRepetitive; }
    private void setRepetitive(boolean repetitive) { isRepetitive = repetitive; }
}
