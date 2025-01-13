package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCsvFileTest {

    @ParameterizedTest
    @Value
    @Test
    void parsingSingleEmployeeSuccessful() {
        String line = """
                Id,firstName,lastName,salary,managerId
                1,Joe,Doe,60000,""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(1, employees.size());
    }

    @Test
    void parsingMultipleEmployeesReturnsSuccessful() {
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

    @Test
    void parseShouldHandleEmptyLines() {
        String line = """
                Id,firstName,lastName,salary,managerId

                1,Joe,Doe,60000,""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(1, employees.size());
    }

    @Test
    void parseShouldHandleNotEnoughColumns() {
        String line = """
                Id,firstName,lastName,salary,managerId
                1,Joe,Doe""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(0, employees.size());
    }

    @Test
    void parseShouldHandleMultipleRecordsWithSameId() {
        String line = """
                Id,firstName,lastName,salary,managerId
                1,Joe,Doe,60000,
                1,Jane,Doe,60000,""";

        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var employees = new SimpleCsvFile(input).parse();
        assertEquals(1, employees.size());
    }

    @Test
    void parseShouldHandleNumberFormatException() {
        String line = """
                Id,firstName,lastName,salary,managerId
                A1,Joe,Doe,60000,
                """;
        var input = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
        var simpleCsvFile = new SimpleCsvFile(input, ParsingErrorHandlingStrategy.THROW_EXCEPTION);
        var ex = assertThrows(CsvParsingException.class, simpleCsvFile::parse);
        assertEquals("""
                Skipping line because invalid number: A1,Joe,Doe,60000,!
                """, ex.getMessage());
    }
}