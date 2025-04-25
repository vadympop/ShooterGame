package com.game.core.exceptions;

/**
 * This exception is thrown when a required configuration is not provided or missing.
 */
public class NotConfiguredException extends RuntimeException {
    public NotConfiguredException(String message) {
        super(message);
    }
}
