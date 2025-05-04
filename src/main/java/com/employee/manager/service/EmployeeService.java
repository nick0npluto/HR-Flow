package com.employee.manager.service;

import com.employee.manager.model.Employee;
import com.employee.manager.repository.EmployeeRepository;
import com.employee.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        try {
            System.out.println("Attempting to update employee with ID: " + id);
            System.out.println("Employee data received: " + employee);
            
            if (employeeRepository.existsById(id)) {
                System.out.println("Employee exists, proceeding with update");
                employee.setEmpid(id);
                Employee saved = employeeRepository.save(employee);
                System.out.println("Employee updated successfully");
                return saved;
            }
            System.out.println("Employee not found with ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getName());
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployees(String firstName, String lastName) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName);
    }

    public Employee getEmployeeBySsn(String ssn) {
        return employeeRepository.findBySsn(ssn).orElse(null);
    }

    public List<Employee> getEmployeesByDateOfBirth(Date dateOfBirth) {
        return employeeRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Employee> getEmployeesByJobTitle(Long jobTitleId) {
        return employeeRepository.findByJobTitle_JobTitleId(jobTitleId);
    }

    public List<Employee> getEmployeesByDivision(Long divisionId) {
        return employeeRepository.findByDivision_Id(divisionId);
    }

    public List<Employee> searchEmployeesFlexible(String firstName, String lastName, String ssn, String dob, Long empid) {
        if (empid != null) {
            return employeeRepository.findByEmpid(empid).map(List::of).orElse(List.of());
        }

        if (ssn != null) {
            return employeeRepository.findBySsn(ssn).map(List::of).orElse(List.of());
        }

        if (dob != null && !dob.isBlank()) {
            try {
                Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
                return employeeRepository.findByDateOfBirth(parsedDate);
            } catch (ParseException e) {
                System.out.println("⚠️ Invalid DOB format: " + dob);
                return List.of(); // return empty if bad format
            }
        }

        if (firstName != null || lastName != null) {
            return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                firstName != null ? firstName : "",
                lastName != null ? lastName : ""
            );
        }

        return List.of(); // No search criteria provided
    }

    @Transactional
    public int updateSalariesInRange(double min, double max, double percent) {
        List<Employee> employees = employeeRepository.findAll().stream()
            .filter(e -> e.getSalary() != null && e.getSalary() >= min && e.getSalary() <= max)
            .toList();
        for (Employee e : employees) {
            double newSalary = e.getSalary() * (1 + percent / 100.0);
            e.setSalary(newSalary);
            System.out.println("Updating employee: " + e.getEmpid() + ", new salary: " + newSalary);
            // Print required fields to check for nulls
            System.out.println("Email: " + e.getEmail() + ", SSN: " + e.getSsn() + ", FirstName: " + e.getFirstName() + ", LastName: " + e.getLastName());
        }
        employeeRepository.saveAll(employees);
        return employees.size();
    }

    public Employee getEmployeeByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(com.employee.manager.model.User::getEmployee)
            .orElse(null);
    }
} 