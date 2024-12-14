package com.epam.reporter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param args program arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Csv filename with employee data is required!");
            System.out.println("Usage: java -jar target/reporter-0.0.1-SNAPSHOT.jar employee.csv");
            System.exit(-1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            Map<Integer, Employee> employees = CsvParser.parseCSV(br);
            List<String> report = new Reporter(employees).report();
            report.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("File " + args[0] + " not found!");
        }
    }
}
