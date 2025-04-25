package com.game.core.exceptions;

/**
 * Exception thrown to indicate that an invalid parameter was provided.
 */
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message) {
        super(message);
    }
}
