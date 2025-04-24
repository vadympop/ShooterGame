package com.game.gui.scenes.game;

import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.Timer;
import com.game.gui.scenes.SceneManager;
import com.game.gui.utils.GameLoop;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;


public class GameController {
    private static final String[] KEYS = {"W", "Up", "Space", "Backspace"};
    private final SceneManager sm;
    private final GameView view;
    private final GameModel model;
    private GameLoop loop;

    private boolean isOnPause = false;

    public Timer<GameController> getMainTimer() {
        return mainTimer;
    }

    private Timer<GameController> mainTimer;

    public GameController(GameView view, GameModel model, SceneManager sm) {
        view.setController(this);

        this.view = view;
        this.model = model;
        this.sm = sm;
    }

    public void start() {
        createMainTimer();
        view.show();

        getModel().getSpawners().forEach(Spawner::spawn);

        loop = new GameLoop(this::render, this::update);
        loop.start();
    }

    private void createMainTimer() {
        mainTimer = new Timer<>(120, x -> x.stopLoop());
    }

    public void stopLoop() {
        loop.stop();
    }

    public void togglePause() {
        isOnPause = !isOnPause();
    }

    public void loadMainMenu() {
        sm.loadMenuScene();
    }

    public void render() {
        view.render();
    }

    public void update(double deltaTime) {
        if (isOnPause()) return;

        if (mainTimer != null) mainTimer.update(deltaTime, this, () -> mainTimer = null);
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
    public GameModel getModel() { return model; }
    public SceneManager getSm() { return sm; }
    public boolean isOnPause() { return isOnPause; }
}
