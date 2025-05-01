# Employee Management System

A full-stack application for managing employee data, payroll, and reporting.

## Features

- Employee authentication and authorization
- CRUD operations for employee data
- Payroll management
- Reporting by job title and division
- Employee search functionality
- Role-based access control (Admin and Employee roles)

## Tech Stack

- Backend: Java, Spring Boot, Spring Security, Spring Data JPA
- Database: MySQL
- Frontend: React (to be implemented)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/employee/manager/
│   │       ├── controller/     # REST controllers
│   │       ├── model/          # Entity classes
│   │       ├── repository/     # JPA repositories
│   │       ├── security/       # Security configuration
│   │       ├── service/        # Business logic
│   │       └── EmployeeManagerApplication.java
│   ├── resources/
│   │   └── application.properties
│   └── webapp/                 # Frontend React app (to be implemented)
└── test/                       # Test classes
```

## API Endpoints

### Employee Management
- GET /api/employees - Get all employees
- GET /api/employees/{id} - Get employee by ID
- POST /api/employees - Create new employee
- PUT /api/employees/{id} - Update employee
- DELETE /api/employees/{id} - Delete employee
- GET /api/employees/search - Search employees by various criteria
- GET /api/employees/job-title/{jobTitleId} - Get employees by job title
- GET /api/employees/division/{divisionId} - Get employees by division

## Setup Instructions

1. Clone the repository
2. Configure MySQL database in `application.properties`
3. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
4. Access the application at `http://localhost:8080`

## Security

- Basic authentication is implemented
- Role-based access control for admin and employee roles
- Password encryption using BCrypt

## Future Enhancements

- Implement React frontend
- Add more detailed reporting features
- Implement email notifications
- Add audit logging
- Implement file upload for employee documents 