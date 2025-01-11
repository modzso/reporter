package com.epam.reporter.impl;

import com.epam.reporter.api.Reporter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Class for creating a simple report about salary discrepancies
 * and long reporting lines.
 * <p>
 * It requires a map of employees.</p>
 */
public class SimpleReporter implements Reporter {
    private static final int MAXIMUM_LEVEL = 5;
    private static final BigDecimal TWENTY_PERCENT = new BigDecimal("1.2");
    private static final BigDecimal FIFTY_PERCENT = new BigDecimal("1.5");
    private static final String THE_FOLLOWING_EMPLOYEES_ARE_NOT_IN_THE_HIERARCHY = "The following employees are not in the hierarchy:";
    private static final String EMPLOYEE_S_S_HAS_MORE_THAN_4_MANAGER_BETWEEN_HIM_AND_THE_CEO = "Employee (%s %s) has more than 4 manager between him and the CEO!";
    private static final String MANAGERS_SALARY_IS_MORE_THAN_50_PERCENT_OF_SUBORDINATES_AVERAGE_SALARY = "Manager %s %s salary (%6.2f) is more than %s%% of subordinates average salary by %6.2f";
    private static final String MANAGERS_SALARY_IS_LESS_THAN_20_PERCENT_OF_SUBORDINATES_AVERAGE_SALARY = "Manager %s %s salary (%6.2f) is less than %s%% of subordinates average salary by %6.2f";
    private static final String DELIMITER = ", ";
    private static final String SUFFIX = ".";
    private static final String SPACE = " ";
    private final BigDecimal lowerRangeCoefficient;
    private final String lowerRangePercentage;
    private final BigDecimal upperRangeCoefficient;
    private final String upperRangePercentage;
    private final Map<Integer, EmployeeEntity> employees;
    private final Set<EmployeeEntity> visitedEmployees = new HashSet<>();


    /**
     * Constructs a new reporter with variable range coefficients.
     *
     * @param lowerRangeCoefficient the lower range of the salary gap.
     * @param lowerRangePercentage  the lower range in percentage.
     * @param upperRangeCoefficient the upper range of the salary gap.
     * @param upperRangePercentage  the upper range in percentage.
     * @param employees             map of employees
     */
    SimpleReporter(BigDecimal lowerRangeCoefficient,
                           String lowerRangePercentage,
                           BigDecimal upperRangeCoefficient,
                           String upperRangePercentage,
                           Map<Integer, EmployeeEntity> employees) {
        this.lowerRangeCoefficient = lowerRangeCoefficient;
        this.lowerRangePercentage = lowerRangePercentage;
        this.upperRangeCoefficient = upperRangeCoefficient;
        this.upperRangePercentage = upperRangePercentage;
        this.employees = employees;
    }

    /**
     * Finds the CEO (employee without manager).
     * Starting from the CEO, it starts checking employees.
     * <p>
     * Checks if any manager salary is between 120% - 150% of their direct subordinates.
     * Also checks if there are longer reporting lines where more than 4 manager between
     * them and the CEO.</p>
     *
     * @return list of report lines
     * @throws CEONotFoundException                     if no employee without manager
     * @throws MultipleEmployeesWithoutManagerException if multiple employees without manager
     */
    @Override
    public List<String> report() {
        EmployeeEntity ceo = findTheCeo();
        if (ceo.isManager()) {
            List<String> report = checkManager(ceo);
            addReportAboutEmployeesNotInHierarchy(report);
            return report;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Finds the CEO, the single employee who has no manager.
     *
     * @return the CEO
     * @throws CEONotFoundException                     if no employee without manager
     * @throws MultipleEmployeesWithoutManagerException if multiple employees without manager
     */
    private EmployeeEntity findTheCeo() {
        List<EmployeeEntity> withoutManager = employees.values()
                .stream()
                .filter(employee -> employee.getManager() == null)
                .toList();
        return withoutManager.getFirst();
    }

    /**
     * Adds a line about Employees not in the hierarchy.
     *
     * @param report the report.
     */
    private void addReportAboutEmployeesNotInHierarchy(List<String> report) {
        List<EmployeeEntity> notVisitedEmployees = getNotVisitedEmployees();

        if (!notVisitedEmployees.isEmpty()) {
            StringJoiner joiner = new StringJoiner(DELIMITER, THE_FOLLOWING_EMPLOYEES_ARE_NOT_IN_THE_HIERARCHY, SUFFIX);
            for (EmployeeEntity employee : notVisitedEmployees) {
                joiner.add(employee.getFirstName() + SPACE + employee.getLastName());
            }
            report.add(joiner.toString());
        }
    }

    /**
     * Checks if manager salary is between 120% - 150% percent
     * between the average of its direct subordinates.
     * Otherwise, it adds a line about it to the report.
     * <p>
     * Also checks if any given reporting line is longer
     * than 4 managers between the CEO and the given employee.
     * If yes, adds a line about it to the report.
     * </p>
     *
     * @param manager manager to be checked
     * @return lines about issues found.
     */
    private List<String> checkManager(EmployeeEntity manager) {
        List<String> report = new ArrayList<>();
        BigDecimal subordinatesAverageSalary = calculateSubordinatesAverageSalaryWithHighPrecision(manager);

        if (manager.getSalary().compareTo(subordinatesAverageSalary.multiply(lowerRangeCoefficient)) < 0) {
            report.add(getLowSalaryReport(manager, subordinatesAverageSalary));
        }
        if (manager.getSalary().compareTo(subordinatesAverageSalary.multiply(upperRangeCoefficient)) > 0) {
            report.add(getHighSalaryReport(manager, subordinatesAverageSalary));
        }
        visitedEmployees.add(manager);
        for (EmployeeEntity employee : manager.getSubordinates()) {
            visitedEmployees.add(employee);
            if (employee.isManager()) {
                report.addAll(checkManager(employee));
            }
            if (employee.getLevel() > MAXIMUM_LEVEL) {
                report.add(getLongReportingLine(employee));
            }
        }
        return report;
    }

    /**
     * Calculates the average salary of the subordinates of the given manager with high precision.
     *
     * @param manager whose subordinates average salary should be calculated.
     * @return average salary of the subordinates
     */
    private static BigDecimal calculateSubordinatesAverageSalaryWithHighPrecision(EmployeeEntity manager) {
        BigDecimal[] totalWithCount = manager.getSubordinates()
                .stream()
                .map(EmployeeEntity::getSalary)
                .map(bd -> new BigDecimal[]{bd, BigDecimal.ONE})
                .reduce((a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(BigDecimal.ONE)})
                .orElseThrow();
        return totalWithCount[0].divide(totalWithCount[1], RoundingMode.HALF_UP);
    }

    /**
     * Returns employees not in the hierarchy.
     *
     * @return list of employees who cannot be reached from the CEO.
     */
    private List<EmployeeEntity> getNotVisitedEmployees() {
        List<EmployeeEntity> notVisitedEmployees = new ArrayList<>(employees.values());
        notVisitedEmployees.removeAll(visitedEmployees);
        return notVisitedEmployees;
    }

    /**
     * Creates a report about long reporting lines.
     *
     * @param employee who has more than 4 manager.
     * @return a report
     */
    private static String getLongReportingLine(EmployeeEntity employee) {
        return String.format(EMPLOYEE_S_S_HAS_MORE_THAN_4_MANAGER_BETWEEN_HIM_AND_THE_CEO,
                employee.getFirstName(), employee.getLastName());
    }

    /**
     * Creates a report about a high salary, that is the manager earns more than 50% of his/her subordinates.
     *
     * @param manager                   whose salary should be checked
     * @param subordinatesAverageSalary the average salary of the manager subordinates
     * @return the report line about the anomaly
     */
    private String getHighSalaryReport(EmployeeEntity manager, BigDecimal subordinatesAverageSalary) {
        return String.format(MANAGERS_SALARY_IS_MORE_THAN_50_PERCENT_OF_SUBORDINATES_AVERAGE_SALARY,
                manager.getFirstName(), manager.getLastName(), manager.getSalary(), upperRangePercentage,
                (manager.getSalary().subtract(subordinatesAverageSalary.multiply(FIFTY_PERCENT))));
    }

    /**
     * Creates a report about low salary, that is the manager earns less than 20% of his/her subordinates.
     *
     * @param manager                   whose salary should be checked
     * @param subordinatesAverageSalary the average salary of the manager subordinates
     * @return the report line about the anomaly
     */
    private String getLowSalaryReport(EmployeeEntity manager, BigDecimal subordinatesAverageSalary) {
        return String.format(MANAGERS_SALARY_IS_LESS_THAN_20_PERCENT_OF_SUBORDINATES_AVERAGE_SALARY,
                manager.getFirstName(), manager.getLastName(), manager.getSalary(), lowerRangePercentage,
                (subordinatesAverageSalary.multiply(TWENTY_PERCENT).subtract(manager.getSalary())));
    }
}
