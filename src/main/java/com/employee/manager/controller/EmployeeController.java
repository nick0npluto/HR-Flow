package com.employee.manager.controller;

import com.employee.manager.model.Employee;
import com.employee.manager.model.JobTitle;
import com.employee.manager.model.Division;
import com.employee.manager.model.Department;
import com.employee.manager.service.EmployeeService;
import com.employee.manager.repository.JobTitleRepository;
import com.employee.manager.repository.DivisionRepository;
import com.employee.manager.dto.EmployeeDTO;
import com.employee.manager.mapper.EmployeeMapper;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobTitleRepository jobTitleRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/salary-update")
    public ResponseEntity<?> updateSalariesInRange(
        @RequestParam double min,
        @RequestParam double max,
        @RequestParam double percent
    ) {
        int updated = employeeService.updateSalariesInRange(min, max, percent);
        return ResponseEntity.ok(Map.of(
            "updatedCount", updated,
            "message", "Salaries updated by " + percent + "% for employees in range [" + min + ", " + max + "]"
        ));
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employee != null
            ? ResponseEntity.ok(employeeMapper.toDTO(employee))
            : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(employeeMapper.toDTO(savedEmployee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return ResponseEntity.notFound().build();
        }
        
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setEmpid(id);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(employeeMapper.toDTO(updatedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeService.getEmployeeById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String ssn,
        @RequestParam(required = false) String dob,
        @RequestParam(required = false) Long empid
    ) {
        List<Employee> results = employeeService.searchEmployeesFlexible(firstName, lastName, ssn, dob, empid);
        return results.isEmpty()
            ? ResponseEntity.notFound().build()
            : ResponseEntity.ok(results);
    }

    @GetMapping("/job-title/{jobTitleId}")
    public List<EmployeeDTO> getEmployeesByJobTitle(@PathVariable Long jobTitleId) {
        return employeeService.getEmployeesByJobTitle(jobTitleId).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/division/{divisionId}")
    public List<EmployeeDTO> getEmployeesByDivision(@PathVariable Long divisionId) {
        return employeeService.getEmployeesByDivision(divisionId).stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeDTO> getCurrentEmployee(Authentication authentication) {
        String username = authentication.getName();
        Employee employee = employeeService.getEmployeeByUsername(username);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeMapper.toDTO(employee));
    }
}
