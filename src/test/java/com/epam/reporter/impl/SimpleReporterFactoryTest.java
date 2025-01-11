package com.epam.reporter.impl;

import com.epam.reporter.api.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.reporter.impl.TestConstants.*;

public class SimpleReporterFactoryTest {
    private static final BigDecimal LESS_THAN_ZERO_COEFFICIENT = new BigDecimal(-1);

    private final SimpleReporterFactory underTest = new SimpleReporterFactory();

    @Test
    public void createReporterRunsJustFine() {
        var employees = List.of(
                new Employee(1, JOHN, DOE, CEO_SALARY, null),
                new Employee(2, JANE, DOE, DIRECTOR_SALARY, 1),
                new Employee(3, DAN, DOE, DIVISION_DIRECTOR_SALARY, 2),
                new Employee(4, NOAH, DOE, DEPARTMENT_MANAGER_SALARY, 3),
                new Employee(5, ROBERT, DOE, SENIOR_MANAGER_SALARY, 4),
                new Employee(6, EMILY, TAYLOR, MANAGER_SALARY, 5),
                new Employee(7, JACK, DOE, EMPLOYEE_SALARY, 6)
        );

        var employeeRecords = employees.stream().collect(Collectors.toMap(Employee::id, Function.identity()));

        var result = underTest.create(employeeRecords);

        assertNotNull(result);
    }

    @Test
    public void createThrowsExceptionIfNoCeoFound() {
        Employee employee1 = new Employee(1, JOHN, DOE, EMPLOYEE_SALARY, 2);
        Employee employee2 = new Employee(2, JANE, DOE, EMPLOYEE_SALARY, 1);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(CEONotFoundException.class, () -> underTest.create(employees));
    }

    @Test
    public void createThrowsExceptionIfMoreCeoFound() {
        Employee employee1 = new Employee(1, JOHN, DOE, EMPLOYEE_SALARY, null);
        Employee employee2 = new Employee(2, JANE, DOE, EMPLOYEE_SALARY, null);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(MultipleEmployeesWithoutManagerException.class, () -> underTest.create(employees));
    }


    @Test
    public void shouldThrowExceptionIfLowerRangeGreaterThanUpper() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(UPPER_RANGE_COEFFICIENT, LOWER_RANGE_COEFFICIENT, Collections.emptyMap()));
    }

    @Test
    public void shouldThrowExceptionIfLowerRangeLessThanZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LESS_THAN_ZERO_COEFFICIENT, UPPER_RANGE_COEFFICIENT, Collections.emptyMap()));
    }

    @Test
    public void shouldThrowExceptionIfUpperRangeLessThanZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, LESS_THAN_ZERO_COEFFICIENT, Collections.emptyMap()));
    }

    @Test
    public void shouldThrowExceptionIfLowerRangeIsZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(null, UPPER_RANGE_COEFFICIENT, Collections.emptyMap()));
    }

    @Test
    public void shouldThrowExceptionIfUpperRangeIsZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, null, Collections.emptyMap()));
    }

    @Test
    public void shouldThrowExceptionIfEmployeesIsNull() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, UPPER_RANGE_COEFFICIENT, null));
    }
}