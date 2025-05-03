package com.employee.manager.repository;

import com.employee.manager.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    List<Payroll> findByEmployeeEmpid(Long empid);

    List<Payroll> findAllByOrderByPayDateDesc();

    @Query(value = "SELECT jt.job_title AS title, SUM(p.earnings) AS total_pay " +
            "FROM payroll p " +
            "JOIN employees e ON p.empid = e.empid " +
            "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
            "GROUP BY jt.job_title", nativeQuery = true)
    List<Map<String, Object>> getPayrollTotalByJobTitle();

    @Query(value = "SELECT d.name AS division, SUM(p.earnings) AS total_pay " +
            "FROM payroll p " +
            "JOIN employees e ON p.empid = e.empid " +
            "JOIN employee_division ed ON e.empid = ed.empid " +
            "JOIN division d ON ed.div_id = d.id " +
            "GROUP BY d.name", nativeQuery = true)
    List<Map<String, Object>> getPayrollTotalByDivision();
}
