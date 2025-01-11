package com.epam.reporter;


import com.epam.reporter.api.ReportExecutor;
import com.epam.reporter.impl.SimpleCsvFile;
import com.epam.reporter.impl.SimpleReporterFactory;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * ReporterApplication provides usage information and miscellaneous checks for argument.
 */
public class ReporterApplication {

    /**
     * No instance of this class is required.
     */
    private ReporterApplication() {
    }

    /**
     * Usage reporter application: csvFilename.csv
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Only one csv filename with employee data is required!");
            System.out.println("Usage: java -jar target/reporter-0.0.1-SNAPSHOT.jar employee.csv ");
            System.exit(-1);
        }
        try (var input = new FileInputStream(args[0])) {
            new ReportExecutor(new SimpleCsvFile(input), new SimpleReporterFactory()).execute();
        } catch (IOException e) {
            System.err.println("File: " + args[0] + " was not found!");
        }
    }
}
