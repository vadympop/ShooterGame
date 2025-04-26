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


/**
 * The GameController class serves as the main controller for the game logic and interactions.
 * It manages the game flow, including loading the scene, handling user inputs,
 * controlling the game loop, and rendering updates.
 */
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

    /**
     * Generates the game model by loading the scene configuration and initializing
     * the game components for rendering and gameplay.
     */
    private void generateModel() {
        Scaler scaler = Scaler.getInstance();
        SceneConfig config = ConfigManager.getInstance().getConfig();

        GameSceneLoader loader = new GameSceneLoader(config, scaler);
        this.model = loader.loadScene();
    }

    /**
     * Restarts the game by resetting the view, regenerating the model,
     * and starting the game loop.
     */
    public void restart() {
        view.restart();
        generateModel();
        start();
    }

    /**
     * Starts the game by initializing the game loop, showing the game view,
     * and spawning the initial entities in the scene.
     */
    public void start() {
        // In case of map restarting
        if (loop != null) stopLoop();

        createMainTimer();
        view.show();

        getModel().getSpawners().forEach(Spawner::spawn);

        loop = new GameLoop(this::render, this::update);
        loop.start();
    }

    /**
     * Creates the main timer for the game to track and handle the game duration
     * and determine the winner when the timer ends.
     */
    private void createMainTimer() {
        mainTimer = new Timer<>(model.getGameDuration(), x -> {
            view.gameEnd(model.getWinnerPlayerSpawner());
        });
    }

    /**
     * Stops the game loop, pausing all ongoing gameplay updates and rendering.
     */
    public void stopLoop() {
        loop.stop();
    }

    /**
     * Toggles the pause state of the game and pauses or resumes the game loop accordingly.
     */
    public void togglePause() {
        isOnPause = !isOnPause();
        loop.togglePause();
    }

    /**
     * Loads the main menu scene by delegating the call to the SceneManager.
     */
    public void loadMainMenu() {
        sm.loadMenuScene();
    }

    /**
     * Renders the game by passing the current game state, including tiles, spawners,
     * blocks, entities, and player spawners, to the GameView for display.
     */
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

    /**
     * Updates the game state by progressing the timer and model state based on
     * the elapsed time since the last update.
     *
     * @param deltaTime The time elapsed since the last update, in seconds.
     */
    public void update(double deltaTime) {
        if (mainTimer != null) mainTimer.update(deltaTime, this, () -> mainTimer = null);
        model.update(deltaTime);
    }

    /**
     * Handles the key press events and triggers corresponding player actions
     * mapped to specific keys.
     *
     * @param keyEvent The KeyEvent representing the key press input.
     */
    public void onKeyPressed(KeyEvent keyEvent) {
        List<PlayerSpawner> spawners = getModel().getPlayerSpawners();
        for (int i = 0; i < spawners.size(); i++) {
            if (keyEvent.getCode() == KeyCode.getKeyCode(KEYS[i])) {
                spawners.get(i).getPlayer().onKeyPressed();
            }
        }
    }

    /**
     * Handles the key release events and triggers corresponding player actions
     * mapped to specific keys.
     *
     * @param keyEvent The KeyEvent representing the key release input.
     */
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

    /**
     * Gets the GameModel instance representing the current game state.
     *
     * @return The GameModel instance.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Gets the SceneManager instance managing scene transitions.
     *
     * @return The SceneManager instance.
     */
    public SceneManager getSm() {
        return sm;
    }

    public boolean isOnPause() { return isOnPause; }
}
