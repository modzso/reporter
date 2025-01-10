package com.epam.reporter;

import java.util.*;

/**
 * Class for creating a simple report about salary discrepancies
 * and long reporting lines.
 * <p>
 * It requires a map of employees.</p>
 */
public class Reporter {

    private static final int MAXIMUM_LEVEL = 5;
    private static final double TWENTY_PERCENT = 1.2;
    private static final double FIFTY_PERCENT = 1.5;
    private final Map<Integer, Employee> employees;
    private final Set<Employee> visitedEmployees = new HashSet<>();

    /**
     * Constructs a new reporter.
     * @param employees map of employees.
     */
    public Reporter(Map<Integer, Employee> employees) {
        this.employees = employees;
    }


    /**
     * Finds the CEO (employee without manager).
     * If multiple employees found or none, it throws a RuntimeException.
     * Starting from the CEO, it starts checking employees.
     * <p>
     * Checks if any manager salary is between 120% - 150% of their direct subordinates.
     * Also checks if there are longer reporting lines where more than 4 manager between
     * them and the CEO.</p>
     * @return list of report lines
     * @throws RuntimeException if no CEO or multiple CEO (employee without supervisor) found
     */
    public List<String> report() {
        List<Employee> withoutManager = findTheCeo();
        Employee ceo = withoutManager.getFirst();
        if (ceo.isManager()) {
            List<String> report = checkManager(ceo);
            addReportAboutEmployeesNotInHierarchy(report);
            return report;
        } else {
            return Collections.emptyList();
        }
    }

    private List<Employee> findTheCeo() {
        List<Employee> withoutManager = employees.values()
                .stream()
                .filter(employee -> employee.getManager() == null)
                .toList();
        if (withoutManager.isEmpty()) {
            throw new CEONotFoundException();
        }
        if (withoutManager.size() > 1) {
            System.out.println(withoutManager);
            throw new MultipleEmployeesWithoutManagerException();
        }
        return withoutManager;
    }

    private void addReportAboutEmployeesNotInHierarchy(List<String> report) {
        List<Employee> notVisitedEmployees = getNotVisitedEmployees();

        if (!notVisitedEmployees.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ", "The following employees are not in the hierarchy:", ".");
            for (Employee employee : notVisitedEmployees) {
                joiner.add(employee.getFirstName() + " " + employee.getLastName());
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
     * @param manager manager to be checked
     * @return lines about issues found.
     */
    private List<String> checkManager(Employee manager) {
        List<String> report = new ArrayList<>();
        double subordinatesAverageSalary = manager.getSubordinates()
                .stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow();

        if (manager.getSalary() < subordinatesAverageSalary * TWENTY_PERCENT) {
            report.add(getLowSalaryReport(manager, subordinatesAverageSalary));
        }
        if (manager.getSalary() > subordinatesAverageSalary * FIFTY_PERCENT) {
            report.add(getHighSalaryReport(manager, subordinatesAverageSalary));
        }
        visitedEmployees.add(manager);
        for (Employee employee : manager.getSubordinates()) {
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

    public List<Employee> getNotVisitedEmployees() {
        List<Employee> notVisitedEmployees = new ArrayList<>(employees.values());
        notVisitedEmployees.removeAll(visitedEmployees);
        return notVisitedEmployees;
    }

    private static String getLongReportingLine(Employee employee) {
        return String.format("Employee (%s %s) has more than 4 manager between him and the CEO!",
                employee.getFirstName(), employee.getLastName());
    }

    private static String getHighSalaryReport(Employee manager, double subordinatesAverageSalary) {
        return String.format("Manager %s %s salary (%6.2f) is more than 50%% of subordinates average salary by %6.2f",
                manager.getFirstName(), manager.getLastName(), manager.getSalary(),
                (manager.getSalary() - subordinatesAverageSalary * FIFTY_PERCENT));
    }

    private static String getLowSalaryReport(Employee manager, double subordinatesAverageSalary) {
        return String.format("Manager %s %s salary (%6.2f) is less than 20%% of subordinates average salary by %6.2f",
                manager.getFirstName(), manager.getLastName(), manager.getSalary(),
                (subordinatesAverageSalary * TWENTY_PERCENT - manager.getSalary()));
    }
}
