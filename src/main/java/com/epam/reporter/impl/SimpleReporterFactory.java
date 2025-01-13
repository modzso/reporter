package com.epam.reporter.impl;

import com.epam.reporter.api.Employee;
import com.epam.reporter.api.ReporterFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converts Employee records to Employee Entities.
 */
public class SimpleReporterFactory implements ReporterFactory {
    private static final String LOWER_RANGE_SHOULD_BE_LESS_THAN_HIGHER_RANGE = "Lower range should be less than higher range!";
    private static final String LOWER_RANGE_CANNOT_BE_LESS_THAN_0 = "Lower range cannot be less than 0!";
    private static final String UPPER_RANGE_CANNOT_BE_LESS_THAN_0 = "Upper range cannot be less than 0!";
    private static final String COEFFICIENT_CANNOT_BE_NULL = "Coefficient cannot be null!";
    private static final BigDecimal TWENTY_PERCENT = new BigDecimal("1.2");
    private static final BigDecimal FIFTY_PERCENT = new BigDecimal("1.5");
    private static final BigDecimal ONEHUNDRED = new BigDecimal(100);
    private static final String EMPLOYEES_CANNOT_BE_NULL = "Employees cannot be null!";


    /**
     * Default constructor.
     */
    public SimpleReporterFactory() {
    }

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
        return coefficient.multiply(ONEHUNDRED).subtract(ONEHUNDRED).toPlainString();
    }


    /**
     * Converts a map of employee records to employee entities.
     * @param employeeRecords map of employees
     * @return map of employees, with references to their manager and subordinates.
     */
    private Map<Integer, EmployeeEntity> convert(Map<Integer, Employee> employeeRecords) {
        var withoutManager = findEmployeesWithoutManager(employeeRecords);
        var ceo = withoutManager.getFirst().id();
        var employees = getEmployeeEntities(employeeRecords);

        var employeesByManager = employeeRecords
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(e -> e.getValue().managerId() == null ? Integer.valueOf(0) : e.getValue().managerId()));

        for (Map.Entry<Integer, List<Map.Entry<Integer, Employee>>> entry : employeesByManager.entrySet()) {
            var manager = getManager(entry, employees, ceo);
            var subordinates = entry
                    .getValue()
                    .stream()
                    .map(Map.Entry::getKey)
                    .map(employees::get)
                    .toList();

            subordinates.forEach(manager::addSubordinate);
        }

        return employees;
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
     * Finds all employees who do not have any manager.
     * @param employeeRecords records or employees
     * @return all employees who do not have manager
     * @throws CEONotFoundException if no employee found without manager
     * @throws MultipleEmployeesWithoutManagerException if more than one employee found without manager
     */
    private static List<Employee> findEmployeesWithoutManager(Map<Integer, Employee> employeeRecords) {
        var withoutManager = employeeRecords
                .values()
                .stream()
                .filter(employee -> employee.managerId() == null)
                .collect(Collectors.toList());
        if (withoutManager.isEmpty()) {
            throw new CEONotFoundException();
        }
        if (withoutManager.size() > 1) {
            System.out.println(withoutManager);
            throw new MultipleEmployeesWithoutManagerException();
        }
        return withoutManager;
    }

    /**
     * Returns the manager for the given list of employees.
     * @param entry the actual entry being processed
     * @param employees map of employees to obtain the manager
     * @param ceo id of the CEO to use where the entry has no value, so the CEO should be used.
     * @return manager of employees in the entry
     */
    private static EmployeeEntity getManager(Map.Entry<Integer, List<Map.Entry<Integer, Employee>>> entry,
                                             Map<Integer, EmployeeEntity> employees,
                                             int ceo) {
        return entry.getKey() == 0 ? employees.get(ceo) : employees.get(entry.getKey());
    }
}
