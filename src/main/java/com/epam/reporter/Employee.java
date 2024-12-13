package com.epam.reporter;

import java.util.ArrayList;
import java.util.List;

public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private Employee manager;
    private List<Employee> subordinates;

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

    public boolean isManager() {
        return !subordinates.isEmpty();
    }

    public int getLevel() {
        return manager == null ? 0 : manager.getLevel() + 1;
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, firstName='%s', lastName='%s', salary=%.2f, managerId=%s}",
                id, firstName, lastName, salary, manager != null ? manager.getId() : "null");
    }
}

