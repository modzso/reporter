package com.epam.reporter.impl;

/**
 * Thrown if the CSV file cannot be found or read.
 */
public class CsvFileNotFoundException extends IllegalArgumentException {
    /**
     * Constructs the exception with a pre-defined message.
     */
    public CsvFileNotFoundException() {
        super("CSV File not found!");
    }
}
