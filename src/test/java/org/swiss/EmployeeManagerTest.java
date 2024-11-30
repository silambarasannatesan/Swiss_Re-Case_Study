package org.swiss;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeManagerTest {
    private EmployeeManager manager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws IOException, CsvValidationException {
        manager = new EmployeeManager();
        manager.loadEmployees("test_employees.csv");
        manager.loadExpectedSalaries("test_expected_salaries.csv");
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testGetCurrentSalaryById() {
        assertEquals(100000.0, manager.getCurrentSalaryById("M001"), 0.01);
        assertEquals(110000.0, manager.getCurrentSalaryById("M002"), 0.01);
    }

    @Test
    public void testReportSalaries() {
        manager.reportSalaries();
        String output = outContent.toString();
        assertTrue(output.contains("Manager M001 earns less than the expected amount by 20000.0"));

    }

    @Test
    public void testReportLongReportingLines() {
        manager.reportLongReportingLines();
        String output = outContent.toString();
        assertTrue(output.contains("Employee John Doe has the longest reporting line which is too long by 1"));
    }
}
