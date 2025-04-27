package com.game.gui.utils;

public final class TimeUtils {
    private TimeUtils() {}

    public static String formatTime(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}