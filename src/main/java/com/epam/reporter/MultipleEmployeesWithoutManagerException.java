package com.epam.reporter;

/**
 * Thrown if multiple employees without manager have been found.
 */
public class MultipleEmployeesWithoutManagerException extends RuntimeException {

    private static final String MULTIPLE_EMPLOYEES_WITHOUT_MANAGER = "Multiple employees without manager!";

    /**
     * Constructor with pre-defined message.
     */
    public MultipleEmployeesWithoutManagerException() {
        super(MULTIPLE_EMPLOYEES_WITHOUT_MANAGER);
    }
}
