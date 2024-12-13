package com.epam.reporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Reporter {

    private final Map<Integer, Employee> employees;

    public Reporter(Map<Integer, Employee> employees) {
        this.employees = employees;
    }

    // Board wants to make sure that every manager earns
    //at least 20% more than the average salary of its direct subordinates, but no more than 50% more
    //than that average. Company wants to avoid too long reporting lines, therefore we would like to
    //identify all employees which have more than 4 managers between them and the CEO
    public List<String> report() {
        // Identify the CEO
        // Iterate over subordinates
        List<Employee> withoutManager = employees.values().stream().filter(employee -> employee.getManager() == null).toList();
        if (withoutManager.isEmpty()) {
            throw new RuntimeException("CEO not found!");
        }
        if (withoutManager.size() > 1) {
            System.out.println(withoutManager);
            throw new RuntimeException("Multiple employees without manager!");
        }
        Employee ceo = withoutManager.get(0);
        if (ceo.isManager()) {
            return checkManager(ceo);
        } else {
            return Collections.emptyList();
        }
    }

    private List<String> checkManager(Employee manager) {
        // Since employee is a manager, he should have subordinates
        List<String> report = new ArrayList<>();
        double subordinatesAverageSalary = manager.getSubordinates().stream().mapToDouble(Employee::getSalary).average().getAsDouble();

        if (manager.getSalary() < subordinatesAverageSalary * 1.2) {
            report.add(String.format("Manager %s %s salary (%.2f) is less than 20%% of subordinates average salary (%.2f)", manager.getFirstName(), manager.getLastName(), manager.getSalary(), subordinatesAverageSalary));
        }
        if (manager.getSalary() > subordinatesAverageSalary * 1.5) {
            report.add(String.format("Manager %s %s salary (%.2f) is more than 50%% of subordinates average salary (%.2f)", manager.getFirstName(), manager.getLastName(), manager.getSalary(), subordinatesAverageSalary));
        }
        for (Employee employee : manager.getSubordinates()) {
            if (employee.isManager()) {
                report.addAll(checkManager(employee));
            }
            if (employee.getLevel() > 5) {
                report.add(String.format("Employee (%s %s) has more than 4 manager between him and the CEO!", employee.getFirstName(), employee.getLastName()));
            }
        }
        return report;
    }

}
