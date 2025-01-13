package com.epam.reporter.impl;

/**
 * Thrown if CSV file cannot be parsed.
 */
public class CsvParsingException extends IllegalArgumentException {
    /**
     * Constructor with a customizable message.
     * @param s  contains information about the error.
     */
    public CsvParsingException(String s) {
        super(s);
    }
}
