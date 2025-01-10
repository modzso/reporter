package com.epam.reporter.api;

import com.epam.reporter.impl.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class ReportExecutorTest {

    @Test
    public void executeShouldCallBothCsvParserAndReporterFactory() throws IOException {
        var csvParser = Mockito.mock(CsvFile.class);
        var reporterFactory = Mockito.mock(ReporterFactory.class);
        var reporter = Mockito.mock(Reporter.class);
        var employees = new HashMap<Integer, Employee>();
        var report = new ArrayList<String>();

        Mockito.when(csvParser.parse()).thenReturn(employees);
        Mockito.when(reporterFactory.create(employees)).thenReturn(reporter);
        Mockito.when(reporter.report()).thenReturn(report);

        var underTest = new ReportExecutor(csvParser, reporterFactory);

        underTest.execute();

        Mockito.verify(csvParser).parse();
        Mockito.verify(reporterFactory).create(employees);
        Mockito.verify(reporter).report();
    }
}