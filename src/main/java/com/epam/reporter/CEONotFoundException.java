package com.epam.reporter;

/**
 * Thrown if no CEO has been found.
 */
public class CEONotFoundException extends RuntimeException {

    private static final String CEO_NOT_FOUND = "CEO not found!";

    /**
     * Constructs the exception with the predefined message.
     */
    public CEONotFoundException() {
        super(CEO_NOT_FOUND);
    }
}
