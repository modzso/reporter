package com.epam.reporter.impl;

import com.epam.reporter.api.ErrorHandler;

/**
 * {@code ErrorHandler} implementation for CSV Parsing.
 */
public enum ParsingErrorHandlingStrategy implements ErrorHandler {

    /**
     * No operation implementation. Just print the error to the standard error and continue.
     */
    NOOP {
        /**
         * Handles the csv parsing error.
         * @param message info about the error
         */
        @Override
        public void handle(String message) {
            System.err.println(message);
        }
    },

    /**
     * Throws a {@code CsvParsingException}.
     */
    THROW_EXCEPTION {
        /**
         * Handles the csv parsing error.
         * @param message info about the error
         */
        @Override
        public void handle(String message) {
            throw new CsvParsingException(message);
        }
    }
}

