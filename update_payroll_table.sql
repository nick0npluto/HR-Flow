USE employeeData;

-- Drop the existing payroll table
DROP TABLE IF EXISTS payroll;

-- Create the new payroll table with the correct structure
CREATE TABLE payroll (
    payID BIGINT PRIMARY KEY AUTO_INCREMENT,
    payDate DATE,
    earnings DECIMAL(10,2),
    fedTax DECIMAL(10,2),
    fedMed DECIMAL(10,2),
    fedSS DECIMAL(10,2),
    stateTax DECIMAL(10,2),
    retire401k DECIMAL(10,2),
    healthCare DECIMAL(10,2),
    empid INT,
    FOREIGN KEY (empid) REFERENCES employees(empid)
); 