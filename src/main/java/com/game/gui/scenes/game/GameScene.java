package com.game.gui.scenes.game;

import com.game.gui.scenes.Scene;
import com.game.gui.scenes.SceneManager;
import javafx.stage.Stage;

public class GameScene implements Scene {
    private final GameModel model;
    private final GameController controller;
    private final GameView view;
    private final SceneManager sm;

    public GameScene(GameModel model, Stage primaryStage, SceneManager sm) {
        this.model = model;
        this.sm = sm;
        this.view = new GameView(primaryStage);
        this.controller = new GameController(view, model);
    }

    @Override
    public void onEnter() {
        this.controller.start();
    }

    @Override
    public void onExit() {

    }
}
