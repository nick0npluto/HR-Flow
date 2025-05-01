package com.employee.manager.repository;

import com.employee.manager.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeEmpid(Long empid);
    List<Payroll> findAllByOrderByPayDateDesc();
}
