package com.employee.manager.service;

import com.employee.manager.model.Payroll;
import com.employee.manager.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public List<Payroll> getPayrollByEmpId(Long empid) {
        return payrollRepository.findByEmployeeEmpid(empid);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAllByOrderByPayDateDesc();
    }
}
