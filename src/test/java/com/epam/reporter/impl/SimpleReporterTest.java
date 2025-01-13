package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.reporter.impl.TestConstants.*;

class SimpleReporterTest {
    private static final String LOWER_RANGE_PERCENTAGE = "20.0";
    private static final String UPPER_RANGE_PERCENTAGE = "50.0";

    @Test
    public void reportReturnsReportForCeo() {
        EmployeeEntity employee1 = new EmployeeEntity(1, JOHN, DOE, EMPLOYEE_SALARY);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, employee1);

        var reporter = createReporterWithDefaults(employees);

        assertTrue(reporter.report().isEmpty());
    }

    @Test
    public void reportDisplaysManagerSalaryLessThanSubordinatesAverageSalary20Percent() {
        EmployeeEntity manager = new EmployeeEntity(1, JOHN, DOE, EMPLOYEE_SALARY);
        EmployeeEntity employee = new EmployeeEntity(2, JANE, DOE, EMPLOYEE_SALARY);
        manager.addSubordinate(employee);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        var reporter = createReporterWithDefaults(employees);

        assertEquals(List.of("Manager John Doe salary ( 80.00) is less than 20.0% of subordinates average salary by  16.00"), reporter.report());
    }

    @Test
    public void reportDisplaysManagerSalaryMoreThanSubordinatesSaverageSalary50Percent() {
        EmployeeEntity manager = new EmployeeEntity(1, JOHN, DOE, CEO_SALARY);
        EmployeeEntity employee = new EmployeeEntity(2, JANE, DOE, EMPLOYEE_SALARY);
        manager.addSubordinate(employee);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        var reporter = createReporterWithDefaults(employees);
        assertEquals(List.of("Manager John Doe salary (239.00) is more than 50.0% of subordinates average salary by 119.00"), reporter.report());
    }

    @Test
    public void reportChecksSubordinatesUnderTopLevelManager() {
        EmployeeEntity ceo = new EmployeeEntity(1, JOHN, DOE, SENIOR_MANAGER_SALARY);
        EmployeeEntity manager = new EmployeeEntity(2, JANE, DOE, MANAGER_SALARY);
        EmployeeEntity employee = new EmployeeEntity(3, JACK, DOE, EMPLOYEE_SALARY);
        ceo.addSubordinate(manager);
        manager.addSubordinate(employee);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, manager);
        employees.put(3, employee);

        assertEquals(new ArrayList<String>(), createReporterWithDefaults(employees).report());
    }

    @Test
    public void reportChecksSubordinatesUnderMoreLevelManager() {
        EmployeeEntity ceo = new EmployeeEntity(1, JOHN, DOE, CEO_SALARY);
        EmployeeEntity director = new EmployeeEntity(2, JANE, DOE, DIRECTOR_SALARY);
        EmployeeEntity divisionDirector = new EmployeeEntity(3, DAN, DOE, DIVISION_DIRECTOR_SALARY);
        EmployeeEntity departmentManager = new EmployeeEntity(4, NOAH, DOE, DEPARTMENT_MANAGER_SALARY);
        EmployeeEntity seniorManager = new EmployeeEntity(5, ROBERT, DOE, SENIOR_MANAGER_SALARY);
        EmployeeEntity manager = new EmployeeEntity(6, EMILY, TAYLOR, MANAGER_SALARY);
        EmployeeEntity employee = new EmployeeEntity(7, JACK, DOE, EMPLOYEE_SALARY);
        ceo.addSubordinate(director);
        director.addSubordinate(divisionDirector);
        divisionDirector.addSubordinate(departmentManager);
        departmentManager.addSubordinate(seniorManager);
        seniorManager.addSubordinate(manager);
        manager.addSubordinate(employee);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, director);
        employees.put(3, divisionDirector);
        employees.put(4, departmentManager);
        employees.put(5, seniorManager);
        employees.put(6, manager);
        employees.put(7, employee);

        assertEquals(List.of("Employee (Jack Doe) has more than 4 manager between him and the CEO!"), createReporterWithDefaults(employees).report());
    }

    @Test
    public void reportEmployeesNotInHierarchy() {
        EmployeeEntity ceo = new EmployeeEntity(1, JOHN, DOE, DEPARTMENT_MANAGER_SALARY);
        EmployeeEntity manager = new EmployeeEntity(2, EMILY, TAYLOR, MANAGER_SALARY);
        EmployeeEntity employee = new EmployeeEntity(3, JACK, DOE, EMPLOYEE_SALARY);
        EmployeeEntity danglingEmployee1 = new EmployeeEntity(4, LAUREN, SMITH, EMPLOYEE_SALARY);
        EmployeeEntity danglingEmployee2 = new EmployeeEntity(5, BLAKE, THOMPSON, EMPLOYEE_SALARY);
        ceo.addSubordinate(manager);
        manager.addSubordinate(employee);
        manager.addSubordinate(employee);

        danglingEmployee1.addSubordinate(danglingEmployee2);
        danglingEmployee2.addSubordinate(danglingEmployee1);

        Map<Integer, EmployeeEntity> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, manager);
        employees.put(3, employee);
        employees.put(4, danglingEmployee1);
        employees.put(5, danglingEmployee2);

        List<String> report = createReporterWithDefaults(employees).report();
        assertEquals(List.of("The following employees are not in the hierarchy:Lauren Smith, Blake Thompson."), report);
    }

    private static SimpleReporter createReporterWithDefaults(Map<Integer, EmployeeEntity> employees) {
        return new SimpleReporter(LOWER_RANGE_COEFFICIENT, LOWER_RANGE_PERCENTAGE, UPPER_RANGE_COEFFICIENT, UPPER_RANGE_PERCENTAGE, employees);
    }

}