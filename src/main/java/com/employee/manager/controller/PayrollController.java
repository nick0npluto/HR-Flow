package com.employee.manager.controller;

import com.employee.manager.model.Payroll;
import com.employee.manager.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping
    public List<Payroll> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }

    @GetMapping("/{empid}")
    public ResponseEntity<List<Payroll>> getPayrollByEmployee(@PathVariable Long empid) {
        List<Payroll> payrolls = payrollService.getPayrollByEmpId(empid);
        return payrolls.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(payrolls);
    }
}
