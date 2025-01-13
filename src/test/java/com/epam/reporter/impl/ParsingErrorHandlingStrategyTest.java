package com.epam.reporter.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParsingErrorHandlingStrategyTest {

    @Test
    void testNoopDoesNotInterrupt() {
        assertDoesNotThrow(() -> ParsingErrorHandlingStrategy.NOOP.handle("message"));
    }

    @Test
    void testThrowExceptionThrowsAnException() {
        var ex = assertThrows(CsvParsingException.class, () -> ParsingErrorHandlingStrategy.THROW_EXCEPTION.handle("message"));
        assertEquals("message", ex.getMessage());
    }
}