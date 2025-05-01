USE employeeData;

SET FOREIGN_KEY_CHECKS=0;

-- Clear existing data
TRUNCATE TABLE employee_job_titles;
TRUNCATE TABLE employee_division;
TRUNCATE TABLE employees;
TRUNCATE TABLE job_titles;
TRUNCATE TABLE divisions;

-- Insert job titles
INSERT INTO job_titles (job_title_id, job_title)
VALUES 
(100,'software manager'),
(101,'software architect'),
(102,'software engineer'),
(103,'software developer'),
(200,'marketing manager'),
(201,'marketing associate'),
(202,'marketing assistant'),
(900,'Chief Exec. Officer'),
(901,'Chief Finn. Officer'),
(902,'Chief Info. Officer');

-- Insert employees
INSERT INTO employees (empid, Fname, Lname, email, salary, SSN)
VALUES 
(1,'Snoopy', 'Beagle', 'Snoopy@example.com', 45000.00, '111-11-1111'),
(2,'Charlie', 'Brown', 'Charlie@example.com', 48000.00, '111-22-1111'),
(3,'Lucy', 'Doctor', 'Lucy@example.com', 55000.00, '111-33-1111'),
(4,'Pepermint', 'Patti', 'Peppermint@example.com', 98000.00, '111-44-1111'),
(5,'Linus', 'Blanket', 'Linus@example.com', 43000.00, '111-55-1111'),
(6,'PigPin', 'Dusty', 'PigPin@example.com', 33000.00, '111-66-1111'),
(7,'Scooby', 'Doo', 'Scooby@example.com', 78000.00, '111-77-1111'),
(8,'Shaggy', 'Rodgers', 'Shaggy@example.com', 77000.00, '111-88-1111'),
(9,'Velma', 'Dinkley', 'Velma@example.com', 82000.00, '111-99-1111'),
(10,'Daphne', 'Blake', 'Daphne@example.com', 59000.00, '111-00-1111'),
(11,'Bugs', 'Bunny', 'Bugs@example.com', 18000.00, '222-11-1111'),
(12,'Daffy', 'Duck', 'Daffy@example.com', 16000.00, '333-11-1111'),
(13,'Porky', 'Pig', 'Porky@example.com', 16550.00, '444-11-1111'),
(14,'Elmer', 'Fudd', 'Elmer@example.com', 15500.00, '555-11-1111'),
(15,'Marvin', 'Martian', 'Marvin@example.com', 28000.00, '777-11-1111');

-- Insert divisions
INSERT INTO divisions (division_id, division_name)
VALUES
(1,'Technology Engineering'),
(2,'Marketing'),
(3,'Human Resources'),
(999,'HQ');

-- Insert employee divisions
INSERT INTO employee_division (empid, div_id)
VALUES
(1,999),
(2,999),
(3,999),
(7,1),
(10,1);

-- Insert employee job titles
INSERT INTO employee_job_titles (empid, job_title_id)
VALUES
(1,902),
(2,900),
(3,901),
(4,102),
(5,101),
(6,201),
(7,100),
(8,102),
(9,102),
(10,102),
(11,200),
(12,201),
(13,202),
(14,103),
(15,103);

SET FOREIGN_KEY_CHECKS=1; 