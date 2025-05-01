USE employeeData;

-- Drop the foreign key constraint if it exists
SET @constraint_name = (SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS 
WHERE TABLE_NAME = 'employees' AND CONSTRAINT_TYPE = 'FOREIGN KEY' 
AND CONSTRAINT_NAME LIKE 'FKiftfevnnkbty768nwfa4nsea8');
SET @sql = IF(@constraint_name IS NOT NULL, 
    CONCAT('ALTER TABLE employees DROP FOREIGN KEY ', @constraint_name), 
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Modify the job_title_id column in employees table to match job_titles table
ALTER TABLE employees MODIFY COLUMN job_title_id INT;

-- Add the foreign key constraint back
ALTER TABLE employees ADD CONSTRAINT FKiftfevnnkbty768nwfa4nsea8 
FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id); 