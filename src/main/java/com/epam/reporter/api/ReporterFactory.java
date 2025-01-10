package com.epam.reporter.api;

import com.epam.reporter.impl.Employee;

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
