package com.epam.reporter.impl;

import com.epam.reporter.api.Employee;
import com.epam.reporter.api.ReporterFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Creates a {@code SimpleReporter} with configurable lower range coefficient and upper range coefficient.
 * Also converts {@code Employee} records to {@code EmployeeEntity} which is linked to his/her managers and subordinates.
 */
public class SimpleReporterFactory implements ReporterFactory {
    private static final String LOWER_RANGE_SHOULD_BE_LESS_THAN_HIGHER_RANGE = "Lower range should be less than higher range!";
    private static final String LOWER_RANGE_CANNOT_BE_LESS_THAN_0 = "Lower range cannot be less than 0!";
    private static final String UPPER_RANGE_CANNOT_BE_LESS_THAN_0 = "Upper range cannot be less than 0!";
    private static final String COEFFICIENT_CANNOT_BE_NULL = "Coefficient cannot be null!";
    private static final BigDecimal TWENTY_PERCENT = new BigDecimal("1.2");
    private static final BigDecimal FIFTY_PERCENT = new BigDecimal("1.5");
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final String EMPLOYEES_CANNOT_BE_NULL = "Employees cannot be null!";
    private static final int DEFAULT_MANAGER_ID_FOR_CEO = 0;

    /**
     * Factory method for creating a {@code SimpleReporter} with default values.
     * @param employees map of employees.
     * @return the created reporter
     */
    @Override
    public SimpleReporter create(Map<Integer, Employee> employees) {
        return create(TWENTY_PERCENT, FIFTY_PERCENT, employees);
    }

    /**
     * Creates a new instance of {@code SimpleReporter} using the provided range coefficients and employees map.
     *
     * @param lowerRangeCoefficient the lower range coefficient used for calculations; must not be null
     * @param upperRangeCoefficient the upper range coefficient used for calculations; must not be null
     * @param employeeRecords a map of employees records where the key is the employee ID and the value is the corresponding {@code Employee} object; must not be null
     * @return a new instance of {@code SimpleReporter}
     * @throws InvalidRangesException if any of the parameters are null or invalid
     */
    public SimpleReporter create(BigDecimal lowerRangeCoefficient,
                                        BigDecimal upperRangeCoefficient,
                                        Map<Integer, Employee> employeeRecords) {

        validateRanges(lowerRangeCoefficient, upperRangeCoefficient);
        validateEmployeesMap(employeeRecords);
        String lowerRangePercentage = toPercentage(lowerRangeCoefficient);
        String upperRangePercentage = toPercentage(upperRangeCoefficient);
        Map<Integer, EmployeeEntity> employees = convert(employeeRecords);
        return new SimpleReporter(lowerRangeCoefficient, lowerRangePercentage, upperRangeCoefficient, upperRangePercentage, employees);
    }

    /**
     * Checks it the employees map is not null.
     * @param employees map of employees
     * @throws IllegalArgumentException if parameter is null
     */
    private static void validateEmployeesMap(Map<Integer, Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException(EMPLOYEES_CANNOT_BE_NULL);
        }
    }

    /**
     * Checks if the lower range coefficient is less than the upper range.
     * @param lowerRangeCoefficient lower range coefficient
     * @param upperRangeCoefficient upper range coefficient
     * @throws InvalidRangesException if the lower range is greater than the upper range
     */
    private static void validateRanges(BigDecimal lowerRangeCoefficient, BigDecimal upperRangeCoefficient) {
        if (lowerRangeCoefficient == null || upperRangeCoefficient == null) {
            throw new InvalidRangesException(COEFFICIENT_CANNOT_BE_NULL);
        }
        if (lowerRangeCoefficient.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRangesException(LOWER_RANGE_CANNOT_BE_LESS_THAN_0);
        }
        if (upperRangeCoefficient.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRangesException(UPPER_RANGE_CANNOT_BE_LESS_THAN_0);
        }
        if (lowerRangeCoefficient.compareTo(upperRangeCoefficient) > 0) {
            throw new InvalidRangesException(LOWER_RANGE_SHOULD_BE_LESS_THAN_HIGHER_RANGE);
        }

    }

    /**
     * Converts number (1.2) to percentage (20%).
     *
     * @param coefficient number to be converted.
     * @return percentage.
     */
    private static String toPercentage(BigDecimal coefficient) {
        return coefficient.multiply(HUNDRED).subtract(HUNDRED).toPlainString();
    }


    /**
     * Converts a map of employee records to employee entities.
     * @param employeeRecords map of employees
     * @return map of employees, with references to their manager and subordinates.
     */
    private Map<Integer, EmployeeEntity> convert(Map<Integer, Employee> employeeRecords) {
        var employees = getEmployeeEntities(employeeRecords);

        var employeesByManager = employeeRecords
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(SimpleReporterFactory::getManagerId));

        for (Map.Entry<Integer, List<Map.Entry<Integer, Employee>>> entry : employeesByManager.entrySet()) {
            var manager = getManager(entry, employees);
            var subordinates = entry
                    .getValue()
                    .stream()
                    .map(Map.Entry::getKey)
                    .map(employees::get)
                    .toList();

            if (manager != null)
                subordinates.forEach(manager::addSubordinate);
        }

        return employees;
    }

    /**
     * Returns the manager id to be used for this {@code Map.Entry}.
     * @param entry employee
     * @return manager id, if null the {@code DEFAULT_MANAGER_ID_FOR_CEO} is returned
     */
    private static Integer getManagerId(Map.Entry<Integer, Employee> entry) {
        return entry.getValue().managerId() == null ? Integer.valueOf(DEFAULT_MANAGER_ID_FOR_CEO) : entry.getValue().managerId();
    }

    /**
     * Creates the initial map of EmployeeEntities.
     * @param employeeRecords records of employees
     * @return map of employee entities
     */
    private static Map<Integer, EmployeeEntity> getEmployeeEntities(Map<Integer, Employee> employeeRecords) {
        return employeeRecords
                .values()
                .stream()
                .map(EmployeeEntity::create)
                .collect(Collectors.toMap(EmployeeEntity::getId, Function.identity()));
    }

    /**
     * Returns the manager for the given list of employees.
     * @param entry the actual entry being processed
     * @param employees map of employees to obtain the manager
     * @return manager of employees in the entry
     */
    private static EmployeeEntity getManager(Map.Entry<Integer, List<Map.Entry<Integer, Employee>>> entry,
                                             Map<Integer, EmployeeEntity> employees) {
        return entry.getKey() == DEFAULT_MANAGER_ID_FOR_CEO ? null : employees.get(entry.getKey());
    }
}
