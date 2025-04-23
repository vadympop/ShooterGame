package com.game.gui.scenes.menu;

import com.game.gui.scenes.Scene;
import com.game.gui.scenes.SceneManager;
import javafx.stage.Stage;

public class MenuScene implements Scene {
    private final MenuModel model;
    private final MenuController controller;
    private final MenuView view;
    private final SceneManager sm;

    public MenuScene(Stage primaryStage, SceneManager sm) {
        this.model = new MenuModel();
        this.sm = sm;
        this.view = new MenuView(primaryStage);
        this.controller = new MenuController(view, model, sm);
    }

    @Override
    public void onEnter() {
        this.controller.start();
    }

    @Override
    public void onExit() {
        this.view.stopAllAnimations();
    }
}
