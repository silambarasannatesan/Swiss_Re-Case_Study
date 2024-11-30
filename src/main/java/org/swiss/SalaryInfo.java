package org.swiss;

public class SalaryInfo {
    private double currentSalary;
    private double expectedSalary;

    public SalaryInfo(double currentSalary, double expectedSalary) {
        this.currentSalary = currentSalary;
        this.expectedSalary = expectedSalary;
    }

    // Getters and setters
    public double getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(double currentSalary) {
        this.currentSalary = currentSalary;
    }

    public double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }
}




