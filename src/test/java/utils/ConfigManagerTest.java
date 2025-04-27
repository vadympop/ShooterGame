package utils;

import com.game.core.enums.GameModeEnum;
import com.game.core.exceptions.InvalidConfigFileException;
import com.game.core.exceptions.InvalidConfigurationException;
import com.game.core.exceptions.InvalidParameterException;
import com.game.core.utils.config.ConfigManager;
import com.game.core.utils.config.SceneConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigManagerTest {
    @Mock private ValidatorFactory validatorFactory;
    @Mock private Validator validator;
    @Mock private ConstraintViolation<SceneConfig> violation;

    private MockedStatic<Validation> validationMock;
    @InjectMocks private ConfigManager configManager;

    @BeforeEach
    void setup() {
        validationMock = mockStatic(Validation.class);
    }

    @AfterEach
    void teardown() {
        validationMock.close();
    }

    @Test
    void getInstanceReturnsNonNullInstance() {
        ConfigManager instance = ConfigManager.getInstance();

        assertNotNull(instance);
    }

    @Test
    void getInstanceReturnsSameInstance() {
        ConfigManager instance1 = ConfigManager.getInstance();
        ConfigManager instance2 = ConfigManager.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void loadSceneConfigSuccessfullyLoadsAndValidatesConfig() throws IOException, URISyntaxException {
        validationMock.when(Validation::buildDefaultValidatorFactory).thenReturn(validatorFactory);
        when(validatorFactory.getValidator()).thenReturn(validator);

        String sceneId = "test";
        when(validator.validate(any(SceneConfig.class))).thenReturn(Collections.emptySet());

        SceneConfig result = configManager.loadSceneConfig(sceneId);

        assertNotNull(result);
        assertSame(result, configManager.getConfig());
        verify(validator).validate(any(SceneConfig.class));
        verifyNoMoreInteractions(validator);
    }

    @Test
    void loadSceneConfigWithInvalidResourceThrowsIOException() {
        String sceneId = "invalid_scene";

        assertThrows(
                InvalidConfigFileException.class,
                () -> configManager.loadSceneConfig(sceneId)
        );
        verifyNoInteractions(validator);
    }

    @Test
    void loadSceneConfigWithInvalidConfigThrowsInvalidConfigurationException() {
        validationMock.when(Validation::buildDefaultValidatorFactory).thenReturn(validatorFactory);
        when(validatorFactory.getValidator()).thenReturn(validator);

        String sceneId = "test";
        when(validator.validate(any(SceneConfig.class))).thenReturn(Set.of(violation));

        InvalidConfigurationException exception = assertThrows(
                InvalidConfigurationException.class,
                () -> configManager.loadSceneConfig(sceneId)
        );
        assertTrue(exception.getMessage().contains("message=Invalid config was provided, check logs, violations={"));
        assertEquals(Set.of(violation), exception.getViolations());
        verify(validator).validate(any(SceneConfig.class));
        verifyNoMoreInteractions(validator);
    }

    @Test
    void loadSceneConfigWithNullSceneIdThrowsNullPointerException() {
        assertThrows(
                InvalidParameterException.class,
                () -> configManager.loadSceneConfig(null)
        );
        verifyNoInteractions(validator);
    }

    @Test
    void getConfigReturnsLoadedConfig() throws IOException, URISyntaxException {
        validationMock.when(Validation::buildDefaultValidatorFactory).thenReturn(validatorFactory);
        when(validatorFactory.getValidator()).thenReturn(validator);

        String sceneId = "test";
        when(validator.validate(any(SceneConfig.class))).thenReturn(Collections.emptySet());
        configManager.loadSceneConfig(sceneId);

        SceneConfig result = configManager.getConfig();

        assertNotNull(result, "getConfig should return the loaded SceneConfig");
        verify(validator).validate(any(SceneConfig.class));
    }

    @Test
    void getConfigBeforeLoadSceneConfigReturnsNull() {
        SceneConfig result = configManager.getConfig();

        assertNull(result, "getConfig should return null before load");
        verifyNoInteractions(validator);
    }

    @Test
    void setGameModeSetsCorrectValue() {
        assertEquals(GameModeEnum.WITH_RELOADING, configManager.getGameMode());

        configManager.setGameMode(GameModeEnum.INFINITY_BULLETS);
        assertEquals(GameModeEnum.INFINITY_BULLETS, configManager.getGameMode());
    }
}