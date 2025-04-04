package com.game.core.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ConfigLoader {
    private final ObjectMapper mapper = new ObjectMapper();
    private SceneConfig config;
    private static ConfigLoader instance;

    private ConfigLoader() {}

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) instance = new ConfigLoader();

        return instance;
    }

    public SceneConfig load(String sceneId) throws IOException, URISyntaxException {
        URL fileURL = getClass().getResource("/scenes/" + sceneId + "_scene.json");
        File file = new File(fileURL.toURI());

        SceneConfig config = mapper.readValue(file, SceneConfig.class);
        setConfig(config);

        return config;
    }

    public SceneConfig getConfig() { return config; }
    private void setConfig(SceneConfig config) { this.config = config; }
}
