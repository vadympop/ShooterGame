package com.game.core.utils;

import com.game.core.entities.Player;

import java.util.function.Consumer;

public class Timer {
    private double baseTime;
    private boolean isRepetitive = false;
    private double timeLeft;
    private Consumer<Player> func;

    public Timer(double time, Consumer<Player> func) {
        setTimeLeft(time);
        setFunc(func);
    }

    public Timer(double time, Consumer<Player> func, boolean isRepetitive) {
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

    public Consumer<Player> getFunc() {
        return func;
    }

    private void setFunc(Consumer<Player> func) {
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
