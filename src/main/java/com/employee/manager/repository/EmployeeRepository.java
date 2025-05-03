package com.employee.manager.repository;

import com.employee.manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmpid(Long empid);
    Optional<Employee> findBySsn(String ssn);
    List<Employee> findByDateOfBirth(Date dateOfBirth);
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Employee> findByJobTitle_JobTitleId(Long jobTitleId);
    List<Employee> findByDivision_Id(Long divisionId);
} 