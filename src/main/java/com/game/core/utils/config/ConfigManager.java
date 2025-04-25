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

/**
 * Singleton class responsible for managing scene configurations and storing the current game mode.
 * Provides functionality to load and validate scene configuration files.
 */
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

    /**
     * Loads the scene configuration for the specified scene ID.
     *
     * @param sceneId The ID of the scene for which the configuration is being loaded.
     * @return The loaded {@link SceneConfig} object.
     * @throws IOException                   If an I/O error occurs while reading the configuration file.
     * @throws URISyntaxException            If the resource URI syntax is incorrect.
     * @throws InvalidParameterException     If the provided scene ID is null.
     * @throws InvalidConfigFileException    If the configuration file is not found or invalid.
     * @throws InvalidConfigurationException If the configuration does not pass validation.
     */
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

    /**
     * Validates the provided {@link SceneConfig} object against its constraints.
     *
     * @param config The {@link SceneConfig} object to be validated.
     * @throws InvalidConfigurationException If the configuration has one or more validation errors.
     */
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
