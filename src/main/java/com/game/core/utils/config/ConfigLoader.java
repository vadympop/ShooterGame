package com.game.core.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.core.exceptions.InvalidConfigurationException;
import com.game.core.exceptions.NotConfiguredException;
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
import java.util.Set;

public class ConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private SceneConfig config;
    private static ConfigLoader instance;

    private ConfigLoader() {}

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) instance = new ConfigLoader();

        return instance;
    }

    public SceneConfig load(String sceneId) throws IOException, URISyntaxException {
        LOGGER.debug("Loading config for {} scene", sceneId);

        URL fileURL = getClass().getResource("/scenes/" + sceneId + "_scene.json");
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
}
