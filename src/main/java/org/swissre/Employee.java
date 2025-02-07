package org.swissre;

public class Employee {
    int id;
    String name;
    double salary;
    Integer managerId;

    public Employee(int id, String name, double salary, Integer managerId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.managerId = managerId;
    }
}
