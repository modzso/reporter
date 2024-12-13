package com.epam.reporter.csv;

import com.epam.reporter.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParser {


    public static Map<Integer, Employee> parseCSV(String filePath) throws IOException {
        Map<Integer, Employee> employeeMap = new HashMap<>();
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                rows.add(parts);

                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                double salary = Double.parseDouble(parts[3].trim());

                Employee employee = new Employee(id, firstName, lastName, salary);
                employeeMap.put(id, employee);
            }
        }

        for (String[] parts : rows) {
            int id = Integer.parseInt(parts[0].trim());
            Employee employee = employeeMap.get(id);
            if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                int managerId = Integer.parseInt(parts[4].trim());
                Employee manager = employeeMap.get(managerId);
                if (manager != null) {
                    employee.setManager(manager);
                    manager.addSubordinate(employee);
                }
            }
        }

        return employeeMap;
    }
}
