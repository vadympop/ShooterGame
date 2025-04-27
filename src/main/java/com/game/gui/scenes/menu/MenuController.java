package com.game.gui.scenes.menu;

import com.game.gui.scenes.SceneManager;

/**
 * Controls the interaction between the menu view, menu model, and scene manager.
 * This class is responsible for handling menu-related logic and scene transitions.
 */
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

    /**
     * Changes the current game scene using the specified scene ID.
     *
     * @param id the identifier of the scene to load
     */
    public void changeGameScene(String id) {
        sm.loadGameScene(id);
    }

    /**
     * Starts the menu by displaying the menu view.
     */
    public void start() {
        view.show();
    }

    /**
     * Retrieves the scene manager associated with this menu controller.
     *
     * @return the {@code SceneManager} instance
     */
    public SceneManager getSm() {
        return sm;
    }
}
