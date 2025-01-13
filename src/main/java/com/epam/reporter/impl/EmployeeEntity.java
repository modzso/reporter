package com.epam.reporter.impl;

import com.epam.reporter.api.Employee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Employee, with name and salary.
 * Also has access to his/her manager and his/her subordinates.
 */
class EmployeeEntity {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private EmployeeEntity manager;
    private final List<EmployeeEntity> subordinates;

    /**
     * Constructor for Employee. Basic properties can be set with this constructor.
     * @param id id of the employee
     * @param firstName first name of the employee
     * @param lastName last name of the employee
     * @param salary salary of the employee
     */
    EmployeeEntity(int id, String firstName, String lastName, BigDecimal salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.subordinates = new ArrayList<>();
    }

    /**
     * Returns the Primary Key.
     * @return primary key
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the employee's manager
     * @param manager the boss
     */
    private void setManager(EmployeeEntity manager) {
        this.manager = manager;
    }

    /**
     * Returns the manager for the employee.
     * @return manager
     */
    public EmployeeEntity getManager() {
        return manager;
    }

    /**
     * Adds a subordinate to this manager.
     * Sets manager on subordiante.
     * @param subordinate employee managed by this Employee.
     */
    public void addSubordinate(EmployeeEntity subordinate) {
        subordinate.setManager(this);
        this.subordinates.add(subordinate);
    }

    /**
     * Returns subordinates for this employee.
     * @return list of subordinate employees
     */
    public List<EmployeeEntity> getSubordinates() {
        return subordinates;
    }

    /**
     * Returns the salary of the employee.
     * @return salary of the employee
     */
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * Returns the first name of the employee
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the employee.
     * @return last name
     */
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


    /**
     * Factory method to instantiate Employee entities from records.
     * @param employee record to use for instances
     * @return the created employee entity
     */
    static EmployeeEntity create(Employee employee) {
        return new EmployeeEntity(employee.id(), employee.firstName(), employee.lastName(), employee.salary());
    }
}

