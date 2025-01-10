package com.epam.reporter;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    @Test
    public void parsing_single_employee_successful() throws IOException {
        String line = """
                Id,firstName,lastName,salary,managerId
                1,Joe,Doe,60000,""";

        Map<Integer, Employee> employees = CsvParser.parseCSV(new BufferedReader(new StringReader(line)));
        assertTrue(employees.size() == 1);
    }

    @Test
    public void parsing_multiple_employees_returns_successful() throws IOException {
        String line = """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                124,Martin,Chekov,45000,123
                125,Bob,Ronstad,47000,123
                300,Alice,Hasacat,50000,124

                305,Brett,Hardleaf,34000,300""";

        Map<Integer, Employee> employees = CsvParser.parseCSV(new BufferedReader(new StringReader(line)));
        assertTrue(employees.size() == 5);
        assertEquals(2, employees.get(123).getSubordinates().size());
    }

}