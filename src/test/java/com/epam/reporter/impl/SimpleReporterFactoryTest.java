package com.epam.reporter.impl;

import com.epam.reporter.api.Employee;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.epam.reporter.impl.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleReporterFactoryTest {
    private static final BigDecimal LESS_THAN_ZERO_COEFFICIENT = new BigDecimal(-1);
    private static final Map<Integer, Employee> EMPTY_MAP = Collections.emptyMap();

    private final SimpleReporterFactory underTest = new SimpleReporterFactory();

    @Test
    void createReporterRunsJustFine() {
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
    void shouldThrowExceptionIfLowerRangeGreaterThanUpper() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(UPPER_RANGE_COEFFICIENT, LOWER_RANGE_COEFFICIENT, EMPTY_MAP));
    }

    @Test
    void shouldThrowExceptionIfLowerRangeLessThanZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LESS_THAN_ZERO_COEFFICIENT, UPPER_RANGE_COEFFICIENT, EMPTY_MAP));
    }

    @Test
    void shouldThrowExceptionIfUpperRangeLessThanZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, LESS_THAN_ZERO_COEFFICIENT, EMPTY_MAP));
    }

    @Test
    void shouldThrowExceptionIfLowerRangeIsZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(null, UPPER_RANGE_COEFFICIENT, EMPTY_MAP));
    }

    @Test
    void shouldThrowExceptionIfUpperRangeIsZero() {
        assertThrows(InvalidRangesException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, null, EMPTY_MAP));
    }

    @Test
    void shouldThrowExceptionIfEmployeesIsNull() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create(LOWER_RANGE_COEFFICIENT, UPPER_RANGE_COEFFICIENT, null));
    }
}