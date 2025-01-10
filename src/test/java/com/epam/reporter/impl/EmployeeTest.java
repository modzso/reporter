package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal(50d);

    @Test
    public void isManagerReturnsTrueIfHasSubordinates() {
        Employee employee = new Employee(1, "first", "last", EMPLOYEE_SALARY);
        Employee subordinate = new Employee(2, "name", "name", EMPLOYEE_SALARY);
        employee.addSubordinate(subordinate);
        subordinate.setManager(employee);

        assertTrue(employee.isManager());
    }

    @Test
    public void isManagerReturnsFalseIfHasNoSubordinates() {
        Employee employee = new Employee(1, "first", "last", EMPLOYEE_SALARY);
        assertFalse(employee.isManager());
    }

    @Test
    public void getLevelReturns0ForTopLevelManager() {
        Employee employee = new Employee(1, "first", "last", EMPLOYEE_SALARY);
        assertEquals(0, employee.getLevel());
    }

    @Test
    public void getLevelReturns1ForEmployeesUnderCeo() {
        Employee manager = new Employee(1, "first", "last", EMPLOYEE_SALARY);
        Employee employee = new Employee(2, "John", "Connor", EMPLOYEE_SALARY);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        assertEquals(1, employee.getLevel());
    }
}