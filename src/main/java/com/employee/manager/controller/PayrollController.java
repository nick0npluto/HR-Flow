package com.employee.manager.controller;

import com.employee.manager.model.Payroll;
import com.employee.manager.model.User;
import com.employee.manager.model.Employee;
import com.employee.manager.service.PayrollService;
import com.employee.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Payroll> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }

    @GetMapping("/{empid}")
    public ResponseEntity<List<Payroll>> getPayrollsByEmployee(@PathVariable Long empid) {
        List<Payroll> payrolls = payrollService.getPayrollByEmpId(empid);
        return payrolls.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(payrolls);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Payroll>> getMyPayroll(Authentication authentication) {
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = optionalUser.get();
        Employee employee = user.getEmployee();

        if (employee == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Payroll> payrolls = payrollService.getPayrollByEmpId(employee.getEmpid());
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/me/paystub")
    public ResponseEntity<InputStreamResource> downloadMyPayStub(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = optionalUser.get();
        Employee employee = user.getEmployee();
        if (employee == null) {
            return ResponseEntity.badRequest().build();
        }

        // Generate CSV content
        StringBuilder sb = new StringBuilder();
        sb.append("Employee ID,Name,Email,Salary\n");
        sb.append(employee.getEmpid()).append(",");
        sb.append(employee.getFirstName()).append(" ").append(employee.getLastName()).append(",");
        sb.append(employee.getEmail()).append(",");
        sb.append(employee.getSalary()).append("\n");

        InputStreamResource resource = new InputStreamResource(
            new java.io.ByteArrayInputStream(sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8))
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=paystub.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(resource);
    }
}
