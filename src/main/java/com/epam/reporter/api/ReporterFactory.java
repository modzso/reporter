package com.epam.reporter.api;


import java.util.Map;

/**
 * Reporter Factory.
 */
public interface ReporterFactory {

    /**
     * Creates a new Reprter with the map of employees.
     * @param employees map of employees.
     * @return Reporter object
     */
    Reporter create(Map<Integer, Employee> employees);
}
