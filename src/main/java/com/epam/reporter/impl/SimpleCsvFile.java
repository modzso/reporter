package com.epam.reporter.impl;

import com.epam.reporter.api.CsvFile;
import com.epam.reporter.api.Employee;
import com.epam.reporter.api.ErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides CSV parsing capability.
 */
public final class SimpleCsvFile implements CsvFile {
    private static final String INVALID_LINE_NOT_ENOUGH_COLUMNS_SKIPPING_IT_LINE_S = "Invalid line, not enough columns, skipping it! Line: %s%n";
    private static final String FILE_CONTAINS_MULTIPLE_RECORDS_FOR_ID_D = "File contains multiple records for id: %d!%n";
    private static final String SKIPPING_LINE_BECAUSE_INVALID_NUMBER_S = "Skipping line because invalid number: %s!%n";
    private static final String COLUMN_SEPARATOR = ",";
    private static final int REQUIRED_COLUMNS = 4;
    private static final int ID_COLUMN_INDEX = 0;
    private static final int FIRST_NAME_COLUMN_INDEX = 1;
    private static final int LAST_NAME_COLUMN_INDEX = 2;
    private static final int SALARY_COLUMN_INDEX = 3;
    private static final int MANAGER_COLUMN_INDEX = 4;
    private static final Integer NO_MANAGER = null;

    private final InputStream input;
    private final ErrorHandler errorHandler;

    /**
     * Constructs a CSV file based on the supplied InputStream with customizable ErrorHandler.
     * @param in from the CSV file
     * @param errorHandler customizable error handler
     */
    public SimpleCsvFile(InputStream in, ErrorHandler errorHandler) {
        this.input = in;
        this.errorHandler = errorHandler;
    }

    /**
     * Constructs a CSV file based on the supplied InputStream.
     * Error handler is set to {@code ParsingErrorHandlerStrategy.NOOP}.
     * @param in the input stream
     */
    public SimpleCsvFile(InputStream in) {
        this(in, ParsingErrorHandlingStrategy.NOOP);
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
    public Map<Integer, Employee> parse() {
        Map<Integer, Employee> employeeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(COLUMN_SEPARATOR);
                if (parts.length < REQUIRED_COLUMNS) {
                    errorHandler.handle(INVALID_LINE_NOT_ENOUGH_COLUMNS_SKIPPING_IT_LINE_S.formatted(line));
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[ID_COLUMN_INDEX].trim());
                    var firstName = parts[FIRST_NAME_COLUMN_INDEX].trim();
                    var lastName = parts[LAST_NAME_COLUMN_INDEX].trim();
                    var salary = new BigDecimal(parts[SALARY_COLUMN_INDEX].trim());

                    var employee = createEmployee(parts, id, firstName, lastName, salary);
                    if (employeeMap.containsKey(id)) {
                        errorHandler.handle(FILE_CONTAINS_MULTIPLE_RECORDS_FOR_ID_D.formatted(id));
                        continue;
                    }
                    employeeMap.put(id, employee);
                } catch (NumberFormatException e) {
                    errorHandler.handle(SKIPPING_LINE_BECAUSE_INVALID_NUMBER_S.formatted(line));
                }
            }
        } catch (IOException e) {
            throw new CsvFileNotFoundException();
        }
        return employeeMap;
    }

    /**
     * Creates employee
     * @param parts containing the managerId
     * @param id primary key for the employee
     * @param firstName first name of the employee
     * @param lastName last name of the employee
     * @param salary salary of the employee
     * @return Employee record created
     */
    private Employee createEmployee(String[] parts, int id, String firstName, String lastName, BigDecimal salary) {
        Employee employee;
        if (hasManagerId(parts)) {
            int managerId = Integer.parseInt(parts[MANAGER_COLUMN_INDEX].trim());
            employee = new Employee(id, firstName, lastName, salary, managerId);
        } else {
            employee = new Employee(id, firstName, lastName, salary, NO_MANAGER);
        }
        return employee;
    }

    /**
     * Returns true if parts array contains a valid managerId
     * @param parts columns for the given line in the file
     * @return true if has a managerId
     */
    private boolean hasManagerId(String[] parts) {
        return parts.length > REQUIRED_COLUMNS && !parts[MANAGER_COLUMN_INDEX].trim().isEmpty();
    }
}
