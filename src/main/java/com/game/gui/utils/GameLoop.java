package com.game.gui.utils;

import javafx.animation.AnimationTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class GameLoop extends AnimationTimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLoop.class);

    private long lastTime = System.nanoTime();
    private long fpsTimer = System.nanoTime();
    private int frames = 0;
    private double deltaTime = 0;
    private final Consumer<Double> updater;
    private final Runnable renderer;

    public GameLoop(Runnable renderer, Consumer<Double> updater) {
        this.updater = updater;
        this.renderer = renderer;
    }

    @Override
    public void handle(long now) {
        deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        frames++;
        if (now - fpsTimer >= 1_000_000_000) {
            int fps = frames;
            frames = 0;
            fpsTimer = now;

            LOGGER.info("FPS: {}", fps);
        }

        updater.accept(deltaTime);
        renderer.run();
    }
}
