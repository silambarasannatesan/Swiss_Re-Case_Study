package org.swiss;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EmployeeManager manager = new EmployeeManager();
        try {
            manager.loadEmployees("employees.csv");
            manager.loadExpectedSalaries("expected_salaries.csv");
            manager.reportSalaries();
            manager.reportLongReportingLines();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}


