# Swiss_Re-Case_Study
This program analyzes a company's hierarchy from a CSV file, ensuring a single CEO, evaluating manager salaries, and detecting excessively long reporting lines. It identifies underpaid or overpaid managers and employees with more than four managers between them and the CEO, providing insights to optimize the organizational structure

**Assumptions:**
1. If a manager's salary is too high or too low compared to their team, the difference has been reported.
2. If an employee has 5 or more levels of managers above them, it has been flagged.
3. If multiple employees do not have a manager listed, the highest-paid person is chosen as the CEO, and the others are placed under their leadership 