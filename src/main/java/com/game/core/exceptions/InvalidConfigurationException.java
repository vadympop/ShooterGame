package com.game.core.exceptions;

import com.game.core.utils.config.SceneConfig;
import jakarta.validation.ConstraintViolation;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Exception thrown when a configuration for a {@link SceneConfig} instance is deemed invalid.
 * This can occur when validation fails for one or more configuration constraints.
 */
public class InvalidConfigurationException extends RuntimeException {
    private final Collection<ConstraintViolation<SceneConfig>> violations;

    public InvalidConfigurationException(String message, Collection<ConstraintViolation<SceneConfig>> violations) {
        super(message);
        this.violations = violations;
    }

    /**
     * Returns a detailed message for the exception, including the original message and
     * a summary of all validation constraint violations.
     *
     * @return a string representation of the exception message and related violations.
     */
    @Override
    public String getMessage() {
        String msg = super.getMessage();
        String violationsStr = violations.stream()
                .map(x -> x.getPropertyPath() + "->" + x.getMessage())
                .collect(Collectors.joining(",\n"));
        return "message=" + msg + ", " + "violations={" + violationsStr + "}";
    }

    /**
     * Retrieves the collection of constraint violations that caused this exception.
     *
     * @return a collection of {@link ConstraintViolation} instances related to the {@link SceneConfig}.
     */
    public Collection<ConstraintViolation<SceneConfig>> getViolations() {
        return violations;
    }
}
