package com.game.gui.scenes.menu;

import com.game.gui.scenes.SceneManager;

public class MenuController {
    private final MenuView view;
    private final MenuModel model;
    private final SceneManager sm;

    public MenuController(MenuView view, MenuModel model, SceneManager sm) {
        view.setController(this);
        this.view = view;
        this.model = model;
        this.sm = sm;
    }

    public void changeGameScene(String id) {
        sm.loadGameScene(id);
    }

    public void start() {
        view.show();
    }

    public SceneManager getSm() {
        return sm;
    }
}
