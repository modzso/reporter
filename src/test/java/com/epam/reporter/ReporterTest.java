package com.epam.reporter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReporterTest {

    private static final BigDecimal CEO_SALARY = new BigDecimal("239");
    private static final BigDecimal DIRECTOR_SALARY = new BigDecimal("199.1");
    private static final BigDecimal DIVISION_DIRECTOR_SALARY = new BigDecimal("165.9");
    private static final BigDecimal DEPARTMENT_MANAGER_SALARY = new BigDecimal("138.24");
    private static final BigDecimal SENIOR_MANAGER_SALARY = new BigDecimal("115.2");
    private static final BigDecimal MANAGER_SALARY = new BigDecimal("96");
    private static final BigDecimal EMPLOYEE_SALARY = new BigDecimal("80");

    @Test
    public void reportThrowsExceptionIfNoCeoFound() {
        Employee employee1 = new Employee(1, "John", "Doe", EMPLOYEE_SALARY);
        Employee employee2 = new Employee(2, "Jane", "Doe", EMPLOYEE_SALARY);
        employee2.setManager(employee1);
        employee1.setManager(employee2);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(CEONotFoundException.class, () -> new Reporter(employees).report());
    }

    @Test
    public void reportThrowsExceptionIfMoreCeoFound() {
        Employee employee1 = new Employee(1, "John", "Doe", EMPLOYEE_SALARY);
        Employee employee2 = new Employee(2, "Jane", "Doe", EMPLOYEE_SALARY);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);
        employees.put(2, employee2);

        assertThrows(MultipleEmployeesWithoutManagerException.class, () -> new Reporter(employees).report());
    }

    @Test
    public void reportReturnsReportForCeo() {
        Employee employee1 = new Employee(1, "John", "Doe", EMPLOYEE_SALARY);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, employee1);

        assertTrue(new Reporter(employees).report().isEmpty());
    }

    @Test
    public void reportDisplaysManagerSalaryLessThanSubordinatesAverageSalary20Percent() {
        Employee manager = new Employee(1, "John", "Doe", EMPLOYEE_SALARY);
        Employee employee = new Employee(2, "Jane", "Doe", EMPLOYEE_SALARY);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        assertEquals(List.of("Manager John Doe salary ( 80.00) is less than 20% of subordinates average salary by  16.00"), new Reporter(employees).report());
    }

    @Test
    public void reportDisplaysManagerSalaryMoreThanSubordinatesSaverageSalary50Percent() {
        Employee manager = new Employee(1, "John", "Doe", CEO_SALARY);
        Employee employee = new Employee(2, "Jane", "Doe", EMPLOYEE_SALARY);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, manager);
        employees.put(2, employee);

        assertEquals(List.of("Manager John Doe salary (239.00) is more than 50% of subordinates average salary by 119.00"), new Reporter(employees).report());
    }

    @Test
    public void reportChecksSubordinatesUnderTopLevelManager() {
        Employee ceo = new Employee(1, "John", "Doe", SENIOR_MANAGER_SALARY);
        Employee manager = new Employee(2, "Jane", "Doe", MANAGER_SALARY);
        Employee employee = new Employee(3, "Jack", "Doe", EMPLOYEE_SALARY);
        ceo.addSubordinate(manager);
        manager.setManager(ceo);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, manager);
        employees.put(3, employee);

        assertEquals(new ArrayList<String>(), new Reporter(employees).report());
    }

    @Test
    public void reportChecksSubordinatesUnderMoreLevelManager() {
        Employee ceo = new Employee(1, "John", "Doe", CEO_SALARY);
        Employee director = new Employee(2, "Jane", "Doe", DIRECTOR_SALARY);
        Employee divisionDirector = new Employee(3, "Dan", "Doe", DIVISION_DIRECTOR_SALARY);
        Employee departmentManager = new Employee(4, "Noah", "Doe", DEPARTMENT_MANAGER_SALARY);
        Employee seniorManager = new Employee(5, "Robert", "Doe", SENIOR_MANAGER_SALARY);
        Employee manager = new Employee(6, "Emily", "Taylor", MANAGER_SALARY);
        Employee employee = new Employee(7, "Jack", "Doe", EMPLOYEE_SALARY);
        ceo.addSubordinate(director);
        director.setManager(ceo);
        director.addSubordinate(divisionDirector);
        divisionDirector.setManager(director);
        divisionDirector.addSubordinate(departmentManager);
        departmentManager.setManager(divisionDirector);
        departmentManager.addSubordinate(seniorManager);
        seniorManager.setManager(departmentManager);
        seniorManager.addSubordinate(manager);
        manager.setManager(seniorManager);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, director);
        employees.put(3, divisionDirector);
        employees.put(4, departmentManager);
        employees.put(5, seniorManager);
        employees.put(6, manager);
        employees.put(7, employee);

        assertEquals(List.of("Employee (Jack Doe) has more than 4 manager between him and the CEO!"), new Reporter(employees).report());
    }

    @Test
    public void reportEmployeesNotInHiearachy() {
        Employee ceo = new Employee(1, "John", "Doe", DEPARTMENT_MANAGER_SALARY);
        Employee manager = new Employee(2, "Emily", "Taylor", MANAGER_SALARY);
        Employee employee = new Employee(3, "Jack", "Doe", EMPLOYEE_SALARY);
        Employee danglingEmployee1 = new Employee(4, "Lauren", "Smith", EMPLOYEE_SALARY);
        Employee danglingEmployee2 = new Employee(5, "Blake", "Thompson", EMPLOYEE_SALARY);
        ceo.addSubordinate(manager);
        manager.addSubordinate(employee);
        manager.setManager(ceo);
        manager.addSubordinate(employee);
        employee.setManager(manager);

        danglingEmployee1.addSubordinate(danglingEmployee2);
        danglingEmployee1.setManager(danglingEmployee2);
        danglingEmployee2.setManager(danglingEmployee1);
        danglingEmployee2.addSubordinate(danglingEmployee1);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(1, ceo);
        employees.put(2, manager);
        employees.put(3, employee);
        employees.put(4, danglingEmployee1);
        employees.put(5, danglingEmployee2);

        List<String> report = new Reporter(employees).report();
        assertEquals(List.of("The following employees are not in the hierarchy:Lauren Smith, Blake Thompson."), report);
    }

}