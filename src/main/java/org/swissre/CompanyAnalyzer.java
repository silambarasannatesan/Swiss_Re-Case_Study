package org.swissre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CompanyAnalyzer {
    final Map<Integer, Employee> employees = new HashMap<>();
    private final Map<Integer, List<Integer>> hierarchy = new HashMap<>();
    private Integer ceoId = null;

    public void loadEmployeeData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String record;
            boolean isFirstLine = true;
            List<Employee> potentialCEOs = new ArrayList<>();

            while ((record = reader.readLine()) != null) {
                if (record.trim().isEmpty()) continue;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] details = record.split(",");
                if (details.length >= 4) {
                    try {
                        int id = Integer.parseInt(details[0].trim());
                        String firstName = details[1].trim();
                        String lastName = details[2].trim();
                        double salary = Double.parseDouble(details[3].trim());
                        Integer managerId = details.length > 4 && !details[4].trim().isEmpty()
                                ? Integer.parseInt(details[4].trim()) : null;

                        Employee employee = new Employee(id, firstName + " " + lastName, salary, managerId);
                        employees.put(id, employee);

                        if (managerId != null) {
                            hierarchy.computeIfAbsent(managerId, k -> new ArrayList<>()).add(id);
                        } else {
                            potentialCEOs.add(employee);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid record: " + record);
                    }
                }
            }

            assignSingleCEO(potentialCEOs);
        }
    }

    private void assignSingleCEO(List<Employee> potentialCEOs) {
        if (potentialCEOs.isEmpty()) {
            System.err.println("No CEO found! Please check the data.");
            return;
        }

        // Select the highest-paid CEO
        Employee ceo = potentialCEOs.stream().max(Comparator.comparingDouble(e -> e.salary)).orElse(null);
        if (ceo != null) {
            ceoId = ceo.id;
            System.out.printf("Employee %s is identified as the CEO.%n", ceo.name);

            // Reassign other potential CEOs under the chosen CEO
            for (Employee e : potentialCEOs) {
                if (e.id != ceoId) {
                    e.managerId = ceoId;
                    hierarchy.computeIfAbsent(ceoId, k -> new ArrayList<>()).add(e.id);
                    System.out.printf("Employee %s reassigned under CEO %s.%n", e.name, ceo.name);
                }
            }
        }
    }

    public void evaluateSalaries() {
        for (Map.Entry<Integer, List<Integer>> entry : hierarchy.entrySet()) {
            Integer managerId = entry.getKey();
            Employee manager = employees.get(managerId);
            if (manager == null) continue;

            List<Employee> subordinates = entry.getValue().stream()
                    .map(employees::get)
                    .collect(Collectors.toList());

            double avgSalary = subordinates.stream().mapToDouble(e -> e.salary).average().orElse(0);
            double minSalaryThreshold = avgSalary * 1.2;
            double maxSalaryThreshold = avgSalary * 1.5;

            if (manager.salary < minSalaryThreshold) {
                System.out.printf("Manager %s is underpaid by %.2f.%n", manager.name, minSalaryThreshold - manager.salary);
            } else if (manager.salary > maxSalaryThreshold) {
                System.out.printf("Manager %s is overpaid by %.2f.%n", manager.name, manager.salary - maxSalaryThreshold);
            }
        }
    }

    public void assessReportingDepth() {
        List<String> employeesWithLongReporting = new ArrayList<>();

        for (Employee employee : employees.values()) {
            int depth = calculateReportingDepth(employee.id);
            if (depth > 4) {
                employeesWithLongReporting.add(
                        String.format("%s (Depth: %d, Exceeds by %d levels)", employee.name, depth, depth - 4)
                );
            }
        }

        if (!employeesWithLongReporting.isEmpty()) {
            System.out.println("\nEmployees with excessive reporting depth:");
            employeesWithLongReporting.forEach(System.out::println);
        } else {
            System.out.println("\nNo employees exceed the maximum reporting depth.");
        }
    }


    int calculateReportingDepth(int id) {
        Set<Integer> visited = new HashSet<>();
        int depth = 0;
        Employee employee = employees.get(id);

        while (employee != null && employee.managerId != null) {
            if (visited.contains(employee.id)) {
                System.out.println("Cycle detected in hierarchy!");
                return -1;
            }
            visited.add(employee.id);
            depth++;
            employee = employees.get(employee.managerId);
        }
        return depth;
    }

    public static void main(String[] args) throws IOException {
        CompanyAnalyzer analyzer = new CompanyAnalyzer();
        analyzer.loadEmployeeData("D:\\\\Preparation\\\\Swiss_RE\\\\src\\\\main\\\\java\\\\org\\\\swissre\\\\employees.csv");
        analyzer.evaluateSalaries();
        analyzer.assessReportingDepth();
    }
}