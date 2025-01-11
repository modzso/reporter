package com.epam.reporter.impl;

import com.epam.reporter.api.CsvFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides CSV parsing capability.
 */
public final class SimpleCsvFile implements CsvFile {

    private final InputStream input;

    /**
     * Constructs a CSV file based on the InputStream.
     * @param in from the CSV file
     */
    public SimpleCsvFile(InputStream in) {
        this.input = in;
    }

    /**
     * Parses input from the given BufferedReader.
     * It assumes that the first line is the header
     * and the order of columns are:
     * Id,firstName,lastName,salary,managerId
     *
     * @return a map of employee records
     * @throws IllegalArgumentException if there is a problem with the file
     */
    public Map<Integer, com.epam.reporter.api.Employee> parse() {
        Map<Integer, com.epam.reporter.api.Employee> employeeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {

            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0].trim());
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                BigDecimal salary = new BigDecimal(parts[3].trim());

                com.epam.reporter.api.Employee employee;
                if (hasManagerId(parts)) {
                    int managerId = Integer.parseInt(parts[4].trim());
                    employee = new com.epam.reporter.api.Employee(id, firstName, lastName, salary, managerId);
                } else {
                    employee = new com.epam.reporter.api.Employee(id, firstName, lastName, salary, null);
                }
                employeeMap.put(id, employee);
            }
        } catch (IOException e) {
            throw new CsvFileNotFoundException();
        }
        return employeeMap;
    }


    private boolean hasManagerId(String[] parts) {
        return parts.length > 4 && !parts[4].trim().isEmpty();
    }
}
