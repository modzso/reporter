package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCsvFileTest {

    @Test
    public void parsingSingleEmployeeSuccessful() {
        String line = """
                Id,firstName,lastName,salary,managerId
                1,Joe,Doe,60000,""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(1, employees.size());
    }

    @Test
    public void parsingMultipleEmployeesReturnsSuccessful() {
        String line = """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                124,Martin,Chekov,45000,123
                125,Bob,Ronstad,47000,123
                300,Alice,Hasacat,50000,124

                305,Brett,Hardleaf,34000,300""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(5, employees.size());
        assertNotNull(employees.get(123));
        assertEquals("Martin", employees.get(124).firstName());
    }
}