package com.game.core.utils;

import java.util.function.Consumer;

public class Timer<T> {
    private double baseTime;
    private boolean isRepetitive = false;
    private double timeLeft;
    private Consumer<T> func;

    public Timer(double time, Consumer<T> func) {
        setTimeLeft(time);
        setFunc(func);
    }

    public Timer(double time, Consumer<T> func, boolean isRepetitive) {
        if (isRepetitive) setBaseTime(time);

        setRepetitive(isRepetitive);
        setTimeLeft(time);
        setFunc(func);
    }

    public void decreaseTime(double value) {
        setTimeLeft(getTimeLeft() - value);
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(double timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Consumer<T> getFunc() {
        return func;
    }

    private void setFunc(Consumer<T> func) {
        this.func = func;
    }

    public double getBaseTime() {
        return baseTime;
    }

    private void setBaseTime(double baseTime) {
        this.baseTime = baseTime;
    }

    public boolean isRepetitive() {
        return this.isRepetitive;
    }

    private void setRepetitive(boolean repetitive) {
        isRepetitive = repetitive;
    }
}
