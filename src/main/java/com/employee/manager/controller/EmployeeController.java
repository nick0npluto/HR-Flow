package com.employee.manager.controller;

import com.employee.manager.model.Employee;
import com.employee.manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return updatedEmployee != null ? ResponseEntity.ok(updatedEmployee) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam(required = false) String firstName,
                                        @RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String ssn,
                                        @RequestParam(required = false) Date dateOfBirth) {
        if (ssn != null) {
            return List.of(employeeService.getEmployeeBySsn(ssn));
        }
        if (dateOfBirth != null) {
            return employeeService.getEmployeesByDateOfBirth(dateOfBirth);
        }
        return employeeService.searchEmployees(firstName, lastName);
    }

    @GetMapping("/job-title/{jobTitleId}")
    public List<Employee> getEmployeesByJobTitle(@PathVariable Long jobTitleId) {
        return employeeService.getEmployeesByJobTitle(jobTitleId);
    }

    @GetMapping("/division/{divisionId}")
    public List<Employee> getEmployeesByDivision(@PathVariable Long divisionId) {
        return employeeService.getEmployeesByDivision(divisionId);
    }
} 