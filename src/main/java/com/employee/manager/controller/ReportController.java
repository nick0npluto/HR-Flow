package com.employee.manager.controller;

import com.employee.manager.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping("/payroll-by-job-title")
    public ResponseEntity<List<Map<String, Object>>> getTotalPayrollByJobTitle() {
        return ResponseEntity.ok(payrollRepository.getPayrollTotalByJobTitle());
    }

    @GetMapping("/payroll-by-division")
    public ResponseEntity<List<Map<String, Object>>> getTotalPayrollByDivision() {
        return ResponseEntity.ok(payrollRepository.getPayrollTotalByDivision());
    }
}
