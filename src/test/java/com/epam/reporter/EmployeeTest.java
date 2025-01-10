package com.epam.reporter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    public void isManagerReturnsTrueIfHasSubordinates() {
        Employee employee = new Employee(1, "first", "last", 50d);
        Employee subordinate = new Employee(2, "name", "name", 45d);
        employee.addSubordinate(subordinate);
        subordinate.setManager(employee);

        assertTrue(employee.isManager());
    }

    @Test
    public void isManagerReturnsFalseIfHasNoSubordinates() {
        Employee employee = new Employee(1, "first", "last", 50d);
        assertFalse(employee.isManager());
    }

    @Test
    public void getLevelReturns0ForTopLevelManager() {
        Employee employee = new Employee(1, "first", "last", 50d);
        assertEquals(0, employee.getLevel());
    }

    @Test
    public void getLevelReturns1ForEmployeesUnderCeo() {
        Employee manager = new Employee(1, "first", "last", 1d);
        Employee employee = new Employee(2, "John", "Connor", 1d);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        assertEquals(1, employee.getLevel());
    }
}