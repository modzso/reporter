package com.epam.reporter.api;

/**
 * Customizable error handler.
 */
public interface ErrorHandler {
    /**
     * This method should handle the error.
     * Implementors can decide to ignore or throw an exception.
     * @param message information about the error.
     */
    void handle(String message);
}
