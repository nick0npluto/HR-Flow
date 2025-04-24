package com.employee.service;

import com.employee.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeService {
    private static final String EMPLOYEES_FILE = System.getProperty("user.dir") + "/employees.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<Employee> employees = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    static {
        System.out.println("Employee data file path: " + EMPLOYEES_FILE);
        loadEmployees();
        createDefaultEmployeeIfNotExists();
    }

    private static void createDefaultEmployeeIfNotExists() {
        if (employees.stream().noneMatch(e -> e.getUsername().equals("employee1"))) {
            Employee defaultEmployee = new Employee();
            defaultEmployee.setId(idCounter.getAndIncrement());
            defaultEmployee.setFirstName("John");
            defaultEmployee.setLastName("Doe");
            defaultEmployee.setEmail("employee1@company.com");
            defaultEmployee.setDepartment("IT");
            defaultEmployee.setPosition("Software Developer");
            defaultEmployee.setSalary(75000.0);
            defaultEmployee.setUsername("employee1");
            employees.add(defaultEmployee);
            saveEmployees();
            System.out.println("Default employee record created for employee1");
        }
    }

    private static void loadEmployees() {
        try {
            File file = new File(EMPLOYEES_FILE);
            if (file.exists()) {
                employees = objectMapper.readValue(file, new TypeReference<List<Employee>>() {});
                // Set the ID counter to the highest ID + 1
                employees.stream()
                        .mapToLong(Employee::getId)
                        .max()
                        .ifPresent(maxId -> idCounter.set(maxId + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveEmployees() {
        try {
            objectMapper.writeValue(new File(EMPLOYEES_FILE), employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee getEmployeeById(Long id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Employee getEmployeeByUsername(String username) {
        return employees.stream()
                .filter(employee -> employee.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(idCounter.getAndIncrement());
        employees.add(employee);
        saveEmployees();
        return employee;
    }

    public Employee updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(employee.getId())) {
                employees.set(i, employee);
                saveEmployees();
                return employee;
            }
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        employees.removeIf(employee -> employee.getId().equals(id));
        saveEmployees();
    }
} 