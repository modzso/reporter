package com.epam.reporter.api;


import java.util.Map;

/**
 * Handles CSV parsing.
 */
public interface CsvFile {

    /**
     * Parses input from the given BufferedReader.
     * It assumes that the first line is the header
     * and the order of columns are:
     * Id,firstName,lastName,salary,managerId
     *
     * @return a map of employees
     * @throws IllegalArgumentException if there is a problem with the file
     */
    Map<Integer, Employee> parse();
}
