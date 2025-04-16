package com.game.gui.scenes.game;

import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.gui.GameLoop;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;


public class GameController {
    private static final String[] KEYS = {"W", "Up", "SPACE", "BACK_SPACE"};
    private GameView view;
    private GameModel model;
    private GameLoop loop;

    public GameController(GameView view, GameModel model) {
        view.setController(this);
        setView(view);
        setModel(model);
    }

    public void start() {
        view.show();

        getModel().getSpawners().forEach(Spawner::spawn);

        loop = new GameLoop(this::render, this::update);
        loop.start();
    }

    public void render() {
        view.render();
    }

    public void update(double deltaTime) {
        model.update(deltaTime);
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        List<PlayerSpawner> spawners = getModel().getPlayerSpawners();
        for (int i = 0; i < spawners.size(); i++) {
            if (keyEvent.getCode() == KeyCode.getKeyCode(KEYS[i])) {
                spawners.get(i).getPlayer().onKeyPressed();
            }
        }
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        List<PlayerSpawner> spawners = getModel().getPlayerSpawners();
        for (int i = 0; i < spawners.size(); i++) {
            if (keyEvent.getCode() == KeyCode.getKeyCode(KEYS[i])) {
                spawners.get(i).getPlayer().onKeyReleased();
            }
        }
    }

    public GameView getView() { return view; }
    public void setView(GameView view) { this.view = view; }

    public GameModel getModel() { return model; }
    public void setModel(GameModel model) { this.model = model; }
}
