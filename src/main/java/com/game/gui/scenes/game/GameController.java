package com.game.gui.scenes.game;

import com.game.core.scene.graphics.TileType;
import com.game.core.scene.spawners.PlayerSpawner;
import com.game.core.scene.spawners.Spawner;
import com.game.core.utils.GameSceneLoader;
import com.game.core.utils.Scaler;
import com.game.core.utils.Timer;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import com.game.gui.scenes.SceneManager;
import com.game.gui.utils.GameLoop;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;


public class GameController {
    private static final String[] KEYS = {"W", "Up", "Space", "Backspace"};
    private final SceneManager sm;
    private final GameView view;
    private GameModel model;
    private GameLoop loop;

    private boolean isOnPause = false;
    private Timer<GameController> mainTimer;

    public GameController(GameView view, SceneManager sm) {
        view.setController(this);

        generateModel();
        this.view = view;
        this.sm = sm;
    }

    private void generateModel() {
        Scaler scaler = Scaler.getInstance();
        SceneConfig config = ConfigManager.getInstance().getConfig();

        GameSceneLoader loader = new GameSceneLoader(config, scaler);
        this.model = loader.loadScene();
    }

    public void restart() {
        view.restart();
        generateModel();
        start();
    }

    public void start() {
        // In case of map restarting
        if (loop != null) stopLoop();

        createMainTimer();
        view.show();

        getModel().getSpawners().forEach(Spawner::spawn);

        loop = new GameLoop(this::render, this::update);
        loop.start();
    }

    private void createMainTimer() {
        mainTimer = new Timer<>(model.getGameDuration(), x -> {
            view.gameEnd(model.getWinnerPlayerSpawner());
        });
    }

    public void stopLoop() {
        loop.stop();
    }

    public void togglePause() {
        isOnPause = !isOnPause();
        loop.togglePause();
    }

    public void loadMainMenu() {
        sm.loadMenuScene();
    }

    public void render() {
        view.render(
                model.getTilesByType(TileType.BACKGROUND),
                model.getSpawners(),
                model.getBlocks(),
                model.getEntities(),
                model.getTilesByType(TileType.OVERLAY),
                model.getPlayerSpawners()
        );
    }

    public void update(double deltaTime) {
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

    public Timer<GameController> getMainTimer() { return mainTimer; }
    public GameView getView() { return view; }
    public GameModel getModel() { return model; }
    public SceneManager getSm() { return sm; }
    public boolean isOnPause() { return isOnPause; }
}
