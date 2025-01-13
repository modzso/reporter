package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.reporter.impl.TestConstants.*;

class EmployeeEntityTest {
    private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal("50");

    @Test
    public void isManagerReturnsTrueIfHasSubordinates() {
        EmployeeEntity employee = new EmployeeEntity(1, JOHN, DOE, EMPLOYEE_SALARY);
        EmployeeEntity subordinate = new EmployeeEntity(2, JANE, DOE, EMPLOYEE_SALARY);
        employee.addSubordinate(subordinate);

        assertTrue(employee.isManager());
    }

    @Test
    public void isManagerReturnsFalseIfHasNoSubordinates() {
        EmployeeEntity employee = new EmployeeEntity(1, JACK, THOMPSON, EMPLOYEE_SALARY);
        assertFalse(employee.isManager());
    }

    @Test
    public void getLevelReturns0ForTopLevelManager() {
        EmployeeEntity employee = new EmployeeEntity(1, JANE, TAYLOR, EMPLOYEE_SALARY);
        assertEquals(0, employee.getLevel());
    }

    @Test
    public void getLevelReturns1ForEmployeesUnderCeo() {
        EmployeeEntity manager = new EmployeeEntity(1, EMILY, THOMPSON, EMPLOYEE_SALARY);
        EmployeeEntity employee = new EmployeeEntity(2, JOHN, DOE, EMPLOYEE_SALARY);
        manager.addSubordinate(employee);

        assertEquals(1, employee.getLevel());
    }
}