package com.game.core.exceptions;

import com.game.core.utils.config.SceneConfig;
import jakarta.validation.ConstraintViolation;

import java.util.Collection;
import java.util.stream.Collectors;

public class InvalidConfigurationException extends RuntimeException {
    private final Collection<ConstraintViolation<SceneConfig>> violations;

    public InvalidConfigurationException(String message, Collection<ConstraintViolation<SceneConfig>> violations) {
        super(message);
        this.violations = violations;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        String violationsStr = violations.stream()
                .map(x -> x.getPropertyPath() + "->" + x.getMessage())
                .collect(Collectors.joining(",\n"));
        return "message=" + msg + ", " + "violations={" + violationsStr + "}";
    }
}
