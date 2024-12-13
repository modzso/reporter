package com.epam.reporter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReporterTest {

    @Test
    public void report_throws_Exception_if_no_ceo_found() {
        Employee employee1 = new Employee(1, "John", "Doe", 1d);
        Employee employee2 = new Employee(2, "Jane", "Doe", 1d);
        employee2.setManager(employee1);
        employee1.setManager(employee2);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(RuntimeException.class, () -> new Reporter(employees).report());
    }

    @Test
    public void report_throws_Exception_if_more_ceo_found() {
        Employee employee1 = new Employee(1, "John", "Doe", 1d);
        Employee employee2 = new Employee(2, "Jane", "Doe", 1d);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(RuntimeException.class, () -> new Reporter(employees).report());
    }

    @Test
    public void report_returns_report_for_ceo() {
        Employee employee1 = new Employee(1, "John", "Doe", 1d);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        assertTrue(new Reporter(employees).report().isEmpty());
    }

    @Test
    public void report_displays_manager_salary_less_than_subordinates_average_salary_20_percent() {
        Employee manager = new Employee(1, "John", "Doe", 100d);
        Employee employee = new Employee(2, "Jane", "Doe", 100d);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        assertEquals(List.of("Manager John Doe salary (100.00) is less than 20% of subordinates average salary by  20.00"), new Reporter(employees).report());
    }

    @Test
    public void report_displays_manager_salarty_more_than_subordinates_saverage_salary_50_percent() {
        Employee manager = new Employee(1, "John", "Doe", 200d);
        Employee employee = new Employee(2, "Jane", "Doe", 100d);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        assertEquals(List.of("Manager John Doe salary (200.00) is more than 50% of subordinates average salary by  50.00"), new Reporter(employees).report());
    }

    @Test
    public void report_checks_subordinates_under_top_level_manager() {
        Employee ceo = new Employee(1, "John", "Doe", 120d);
        Employee manager = new Employee(2, "Jane", "Doe", 100d);
        Employee employee = new Employee(3, "Jack", "Doe", 80d);
        ceo.addSubordinate(manager);
        manager.setManager(ceo);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, manager);
        employees.put(3, employee);

        assertEquals(new ArrayList<String>(), new Reporter(employees).report());
    }

    @Test
    public void report_checks_subordinates_under_more_level_manager() {
        Employee ceo = new Employee(1, "John", "Doe", 239d);
        Employee director = new Employee(2, "Jane", "Doe", 199.1d);
        Employee divisionDirector = new Employee(3, "Dan", "Doe", 165.9d);
        Employee departmentManager = new Employee(4, "Noah", "Doe", 138.24d);
        Employee seniorManager = new Employee(5, "Robert", "Doe", 115.2d);
        Employee manager = new Employee(6, "Emily", "Taylor", 96d);
        Employee employee = new Employee(7, "Jack", "Doe", 80d);
        ceo.addSubordinate(director);
        director.setManager(ceo);
        director.addSubordinate(divisionDirector);
        divisionDirector.setManager(director);
        divisionDirector.addSubordinate(departmentManager);
        departmentManager.setManager(divisionDirector);
        departmentManager.addSubordinate(seniorManager);
        seniorManager.setManager(departmentManager);
        seniorManager.addSubordinate(manager);
        manager.setManager(seniorManager);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, director);
        employees.put(3, divisionDirector);
        employees.put(4, departmentManager);
        employees.put(5, seniorManager);
        employees.put(6, manager);
        employees.put(7, employee);

        assertEquals(List.of("Employee (Jack Doe) has more than 4 manager between him and the CEO!"), new Reporter(employees).report());
    }

}