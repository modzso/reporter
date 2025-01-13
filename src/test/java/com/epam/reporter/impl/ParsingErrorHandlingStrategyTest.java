package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParsingErrorHandlingStrategyTest {

    @Test
    public void testNoopDoesNotInterrupt() {
        ParsingErrorHandlingStrategy.NOOP.handle("message");
    }

    @Test
    public void testThrowExceptionThrowsAnException() {
        var ex = assertThrows(CsvParsingException.class, () -> ParsingErrorHandlingStrategy.THROW_EXCEPTION.handle("message"));
        assertEquals("message", ex.getMessage());
    }
}