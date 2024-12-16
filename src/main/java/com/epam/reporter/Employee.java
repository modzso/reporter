package com.epam.reporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Employee, with name and salary.
 * Also has access to his/her manager and his/her subordinates.
 */
public class Employee {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private Employee manager;
    private final List<Employee> subordinates;

    /**
     * Constructor for Employee. Basic properties can be set with this constructor.
     * @param id id of the employee
     * @param firstName first name of the employee
     * @param lastName last name of the employee
     * @param salary salary of the employee
     */
    public Employee(int id, String firstName, String lastName, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.subordinates = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Employee getManager() {
        return manager;
    }

    public void addSubordinate(Employee subordinate) {
        this.subordinates.add(subordinate);
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public double getSalary() {
        return salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Retunrs true if the employee is manager, that is she/he has subordinates.
     * @return true if the employee is manager
     */
    public boolean isManager() {
        return !subordinates.isEmpty();
    }

    /**
     * Returns the level of the employee.
     * The CEO (top level employee without manager has level 0.
     * All direct subordinates of the CEO has level 1.
     * All subordinates below increment this level by 1.
     * @return the hierarchy level of the employee.
     */
    public int getLevel() {
        return manager == null ? 0 : manager.getLevel() + 1;
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, firstName='%s', lastName='%s', salary=%.2f, managerId=%s}",
                id, firstName, lastName, salary, manager != null ? manager.getId() : "null");
    }
}

