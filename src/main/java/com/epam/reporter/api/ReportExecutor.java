package com.epam.reporter.api;

/**
 * Executes a report on the given CsvFile.
 */
public class ReportExecutor {
    private final CsvFile csvFile;
    private final ReporterFactory reporterFactory;

    /**
     * Constructs a ReportExecutor.
     * @param csvFile to be used for CSV parsing
     * @param reporterFactory creates a reporter to be used for reporting
     */
    public ReportExecutor(CsvFile csvFile,
                          ReporterFactory reporterFactory) {
        this.csvFile = csvFile;
        this.reporterFactory = reporterFactory;
    }

    /**
     * Generates the report.
     * The report is printed to standard output.
     */
    public void execute() {
        var employees = csvFile.parse();
        var report = reporterFactory.create(employees).report();
        report.forEach(System.out::println);
    }
}
