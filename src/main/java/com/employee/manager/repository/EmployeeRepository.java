package com.employee.manager.repository;

import com.employee.manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    Employee findBySsn(String ssn);
    List<Employee> findByDateOfBirth(Date dateOfBirth);
    List<Employee> findByJobTitleId(Long jobTitleId);
    List<Employee> findByDivisionId(Long divisionId);
} 