package com.employee;

import com.employee.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeManagerApplication {
    @Autowired
    private AuthenticationService authService;

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            try {
                authService.createUser("admin", "admin123", "ADMIN");
                System.out.println("Admin user created successfully");
            } catch (Exception e) {
                System.out.println("Admin user already exists or error creating admin user: " + e.getMessage());
            }
        };
    }
} 