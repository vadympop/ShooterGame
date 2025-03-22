package com.game.core.utils;

import com.game.core.entities.Player;

import java.util.function.Consumer;

public class Timer {
    private double time;
    private Consumer<Player> execute;

    public Timer(double time, Consumer<Player> execute) {
        setTime(time);
        setExecute(execute);
    }

    public void decreaseTime(double value) {
        setTime(getTime() - value);
    }

    public double getTime() {
        return time;
    }

    private void setTime(double time) {
        this.time = time;
    }

    public Consumer<Player> getExecute() {
        return execute;
    }

    private void setExecute(Consumer<Player> execute) {
        this.execute = execute;
    }
}
