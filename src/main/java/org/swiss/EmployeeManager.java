package org.swiss;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EmployeeManager {
    private List<Employee> employees = new ArrayList<>();
    private Map<String, SalaryInfo> salaryInfoMap = new HashMap<>();

    public void loadEmployees(String csvFile) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }
                employees.add(new Employee(line[0], line[1], line[2], line[3], Double.parseDouble(line[4])));
            }
        }
    }

    public void loadExpectedSalaries(String csvFile) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header row
                }
                String id = line[0];
                double expectedSalary = Double.parseDouble(line[1]);
                double currentSalary = getCurrentSalaryById(id);
                salaryInfoMap.put(id, new SalaryInfo(currentSalary, expectedSalary));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    double getCurrentSalaryById(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return employee.getSalary();
            }
        }
        return 0.0;
    }

    public void reportSalaries() {
        System.out.println("Managers earning less than they should:");
        for (Map.Entry<String, SalaryInfo> entry : salaryInfoMap.entrySet()) {
            String id = entry.getKey();
            SalaryInfo salaryInfo = entry.getValue();
            double difference = salaryInfo.getExpectedSalary() - salaryInfo.getCurrentSalary();
            if (difference > 0) {
                System.out.println("Manager " + id + " earns less than the expected amount by " + difference);
            }
        }

        System.out.println("Managers earning more than they should:");
        for (Map.Entry<String, SalaryInfo> entry : salaryInfoMap.entrySet()) {
            String id = entry.getKey();
            SalaryInfo salaryInfo = entry.getValue();
            double difference = salaryInfo.getCurrentSalary() - salaryInfo.getExpectedSalary();
            if (difference > 0) {
                System.out.println("Manager " + id + " earns more than the expected amount by " + difference);
            }
        }
    }

    public void reportLongReportingLines() {
        Employee maxReportingLineEmployee = null;
        int maxReportingLineLength = 0;

        for (Employee employee : employees) {
            int reportingLineLength = getReportingLineLength(employee);
            if (reportingLineLength > maxReportingLineLength) {
                maxReportingLineLength = reportingLineLength;
                maxReportingLineEmployee = employee;
            }
        }

        if (maxReportingLineEmployee != null) {
            System.out.println("Employee " + maxReportingLineEmployee.getFullName() + " has the longest reporting line which is too long by " + (maxReportingLineLength - 3));
        }
    }

    private int getReportingLineLength(Employee employee) {
        int length = 0;
        String managerId = employee.getManagerId();
        Set<String> visited = new HashSet<>();
        while (managerId != null && !managerId.isEmpty() && !visited.contains(managerId)) {
            visited.add(managerId);
            length++;
            Employee manager = getEmployeeById(managerId);
            if (manager != null) {
                managerId = manager.getManagerId();
            } else {
                break;
            }
        }
        return length;
    }

    private Employee getEmployeeById(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return employee;
            }
        }
        return null;
    }
}






