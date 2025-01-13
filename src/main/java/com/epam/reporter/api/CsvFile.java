package com.epam.reporter.api;


import java.util.Map;

/**
 * Handles CSV parsing.
 */
public interface CsvFile {

    /**
     * Returns a {@code Map<Integer, Employee} contained in the file.
     * Implementation should expect a header and handle empty lines.
     *
     * @return a map of employees
     * @throws IllegalArgumentException if there is a problem with the file
     */
    Map<Integer, Employee> parse();
}
