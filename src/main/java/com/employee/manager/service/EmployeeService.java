package com.employee.manager.service;

import com.employee.manager.model.Employee;
import com.employee.manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

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
        if (employeeRepository.existsById(id)) {
            employee.setEmpid(id);
            return employeeRepository.save(employee);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployees(String firstName, String lastName) {
        return employeeRepository.findByFirstNameContainingOrLastNameContaining(firstName, lastName);
    }

    public Employee getEmployeeBySsn(String ssn) {
        return employeeRepository.findBySsn(ssn);
    }

    public List<Employee> getEmployeesByDateOfBirth(Date dateOfBirth) {
        return employeeRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Employee> getEmployeesByJobTitle(Long jobTitleId) {
        return employeeRepository.findByJobTitleId(jobTitleId);
    }

    public List<Employee> getEmployeesByDivision(Long divisionId) {
        return employeeRepository.findByDivisionId(divisionId);
    }
} 