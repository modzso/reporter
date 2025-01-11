package com.epam.reporter.api;

import java.math.BigDecimal;

/**
 * Represents an Employee record in the employees CSV file
 * @param id        primary key for the employee
 * @param firstName first name of the employee
 * @param lastName  last name of the emplouee
 * @param salary    salary of the employee
 * @param managerId id of the manager, can be null for the CEO
 */
public record Employee(int id, String firstName, String lastName, BigDecimal salary, Integer managerId) {
}
