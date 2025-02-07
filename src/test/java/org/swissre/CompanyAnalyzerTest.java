package org.swissre;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyAnalyzerTest {
    private CompanyAnalyzer analyzer;
    private static final String TEST_CSV_FILE = "test_employees.csv";

    @BeforeAll
    void setup() throws IOException {
        analyzer = new CompanyAnalyzer();
        String testCsvData = """
          Id,firstName,lastName,salary,managerId
          101,John,CEO,100000,
          102,Mary,Manager,60000,101
          103,Bob,Senior Manager,50000,102
          104,Alice,Manager,45000,103
          105,Charlie,Lead,42000,104
          106,David,Employee,40000,105
          107,Eve,Intern,30000,106
          """;
        Files.write(Path.of(TEST_CSV_FILE), testCsvData.getBytes());
        analyzer.loadEmployeeData(TEST_CSV_FILE);
    }

    @Test
    void testSalaryEvaluation() {
        List<String> output = new ArrayList<>();
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream()) {
            public void println(String s) { output.add(s); }
        });

        analyzer.evaluateSalaries();

        boolean underpaidDetected = output.stream().anyMatch(s -> s.contains("Manager Bob is underpaid by 4000"));
        assertFalse(underpaidDetected, "Bob should be detected as underpaid by 4000.");
    }

    @Test
    void testReportingDepth() {
        int depth = analyzer.calculateReportingDepth(107); // Eve, an intern
        System.out.println("Calculated depth for Eve: " + depth);
        assertTrue(depth > 4, "Eve should have more than 4 managers above her.");
    }

    @Test
    void testSingleCEOAssignment() {
        long ceoCount = analyzer.employees.values().stream().filter(e -> e.managerId == null).count();
        assertEquals(1, ceoCount, "There should be exactly one CEO.");
    }

    @AfterAll
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_CSV_FILE));
    }
}
