package com.epam.reporter.impl;

/**
 * Thrown if the CSV file cannot be found or read.
 */
public class CsvFileNotFoundException extends IllegalArgumentException {
    public CsvFileNotFoundException() {
        super("CSV File not found!");
    }
}
