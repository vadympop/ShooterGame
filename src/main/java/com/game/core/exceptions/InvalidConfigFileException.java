package com.game.core.exceptions;

/**
 * Exception thrown when the system encounters an invalid configuration file.
 * This exception indicates that the configuration file provided does not meet
 * the expected format or contains invalid data.
 */
public class InvalidConfigFileException extends RuntimeException {
    public InvalidConfigFileException(String message) {
        super(message);
    }
}
