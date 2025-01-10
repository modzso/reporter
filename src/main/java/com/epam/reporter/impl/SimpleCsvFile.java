package com.epam.reporter.impl;

import com.epam.reporter.api.CsvFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class provides CSV parsing capability.
 */
public final class SimpleCsvFile implements CsvFile {

    private final InputStream input;

    public SimpleCsvFile(InputStream in) {
        this.input = in;
    }

    /**
     * Parses input from the given BufferedReader.
     * It assumes that the first line is the header
     * and the order of columns are:
     * Id,firstName,lastName,salary,managerId
     *
     * @return a map of employees
     * @throws IllegalArgumentException if there is a problem with the file
     */
    public Map<Integer, Employee> parse() {
        Map<Integer, Employee> employeeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            List<String[]> rows = new ArrayList<>();

            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                rows.add(parts);

                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                BigDecimal salary = new BigDecimal(parts[3].trim());

                Employee employee = new Employee(id, firstName, lastName, salary);
                employeeMap.put(id, employee);
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

        } catch (IOException e) {
            throw new CsvFileNotFoundException();
        }
        return employeeMap;
    }
}
