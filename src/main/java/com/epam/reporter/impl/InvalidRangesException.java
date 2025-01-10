package com.epam.reporter.impl;

/**
 * Thrown if coefficients are invalid.
 */
public class InvalidRangesException extends IllegalArgumentException {
    /**
     * Creates an instance with a customizable error message.
     * @param s message
     */
    public InvalidRangesException(String s) {
        super(s);
    }
}
