# 1. Overview
This case is designed to manage employee data, including their salaries and reporting lines. The program reads employee and expected salary data from CSV files, and then generates reports on:

  1. Which managers earn less than they should, and by how much.
  2. Which managers earn more than they should, and by how much.
  3. Which employee has the longest reporting line, and by how much it exceeds the threshold

# 2. How to Run
Ensure you have the following CSV files in the same directory as the program:

  1. employees.csv
  2. expected_salaries.csv

The employees.csv file should have the following format:
id,firstName,lastName,managerId,salary
E001,John,Doe,M001,50000
E002,Jane,Smith,M001,60000
E003,Emily,Jones,M002,55000
E004,Michael,Brown,M002,58000
E005,Chris,Johnson,M003,62000
M001,Manager,One,,100000
M002,Manager,Two,,110000
M003,Manager,Three,,105000
M004,Manager,Four,,95000
M005,Manager,Five,,90000


The expected_salaries.csv file should have the following format:  
id,expectedSalary
M001,120000
M002,110000
M003,105000
M004,95000
M005,90000


Compile and run the Main.java file to generate the reports.


# 3. Main.java
The main entry point of the program, which performs the following steps:

Loads employee data from employees.csv.
Loads expected salary data from expected_salaries.csv.
Generates reports on salaries and reporting lines.
Compile and run the EmployeeManagerTest.java file to execute the unit tests.
