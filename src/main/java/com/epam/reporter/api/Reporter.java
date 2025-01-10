package com.epam.reporter.api;

import java.util.List;

/**
 * Creates a report.
 * The report can contain multiple lines.
 */
public interface Reporter {

    /**
     * Retrieves the report.
     * @return list of lines that consist the report.
     */
    List<String> report();
}
