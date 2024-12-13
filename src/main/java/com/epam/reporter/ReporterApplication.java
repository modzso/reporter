package com.epam.reporter;


import com.epam.reporter.csv.CsvParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReporterApplication {

	/**
	 * Usage reporter application: csvFilename.csv
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Csv filename with employee data is required!");
			System.exit(-1);
		}

		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
			Map<Integer, Employee> employees = CsvParser.parseCSV(br);
			List<String> report = new Reporter(employees).report();
			report.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println("File " + args[0] + " not found");
		}
	}
}
