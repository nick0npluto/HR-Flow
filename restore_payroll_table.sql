USE employeeData;

-- Create the original payroll table structure
CREATE TABLE payroll (
    empid INT PRIMARY KEY,
    salary DECIMAL(10,2),
    pay_date DATE,
    FOREIGN KEY (empid) REFERENCES employees(empid)
); 