package com.game.core.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.core.enums.GameModeEnum;
import com.game.core.exceptions.InvalidConfigFileException;
import com.game.core.exceptions.InvalidConfigurationException;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.ResourceUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private SceneConfig config;
    private static ConfigManager instance;

    private GameModeEnum gameMode = GameModeEnum.WITH_RELOADING;

    private ConfigManager() {}

    public static synchronized ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();

        return instance;
    }

    public SceneConfig loadSceneConfig(String sceneId) throws IOException, URISyntaxException {
        LOGGER.debug("Loading config for {} scene", sceneId);
        if (sceneId == null)
            throw new InvalidParameterException("Scene id should not be null");

        URL fileURL = ResourceUtils.getResource("/scenes/" + sceneId + "_scene.json");
        if (fileURL == null)
            throw new InvalidConfigFileException("Config file for " + sceneId + " not found");

        File file = new File(fileURL.toURI());

        SceneConfig config = mapper.readValue(file, SceneConfig.class);
        validateConfig(config);

        setConfig(config);
        return config;
    }

    private void validateConfig(SceneConfig config) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<SceneConfig>> violations = validator.validate(config);

        if (!violations.isEmpty()) {
            throw new InvalidConfigurationException("Invalid config was provided, check logs", violations);
        }
    }

    public SceneConfig getConfig() { return config; }
    private void setConfig(SceneConfig config) { this.config = config; }

    public GameModeEnum getGameMode() { return gameMode; }
    public void setGameMode(GameModeEnum gameMode) {
        this.gameMode = Objects.requireNonNull(gameMode);
    }
}
