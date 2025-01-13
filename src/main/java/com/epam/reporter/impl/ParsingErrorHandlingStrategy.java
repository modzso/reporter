package com.epam.reporter.impl;

import com.epam.reporter.api.ErrorHandler;

import java.util.logging.Logger;

/**
 * {@code ErrorHandler} implementation for CSV Parsing.
 */
public enum ParsingErrorHandlingStrategy implements ErrorHandler {

    /**
     * No operation implementation. Just print the error to the standard error and continue.
     */
    NOOP {
        private static final Logger LOGGER = Logger.getLogger(ParsingErrorHandlingStrategy.class.getName());
        /**
         * Handles the csv parsing error.
         * @param message info about the error
         */
        @Override
        public void handle(String message) {
            LOGGER.warning(message);
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

