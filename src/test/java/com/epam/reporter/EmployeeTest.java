package com.epam.reporter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    public void isManager_Returns_true_if_has_subordinates() {
        Employee employee = new Employee(1, "first", "last", 50d);
        Employee subordinate = new Employee(2, "name", "name", 45d);
        employee.addSubordinate(subordinate);
        subordinate.setManager(employee);

        assertTrue(employee.isManager());
    }

    @Test
    public void isManager_returns_false_if_has_no_subordinates() {
        Employee employee = new Employee(1, "first", "last", 50d);
        assertFalse(employee.isManager());
    }

    @Test
    public void getLevel_Returns_0_for_top_level_manager() {
        Employee employee = new Employee(1, "first", "last", 50d);
        assertEquals(0, employee.getLevel());
    }

    @Test
    public void getLevel_returns_1_for_employees_under_ceo() {
        Employee manager = new Employee(1, "first", "last", 1d);
        Employee employee = new Employee(2, "John", "Connor", 1d);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        assertEquals(1, employee.getLevel());
    }
}