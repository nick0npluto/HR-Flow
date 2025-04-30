package com.employee;

import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.service.AuthenticationService;
import com.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;

public class ApiHandler implements HttpHandler {
    private static final Logger logger = Logger.getLogger(ApiHandler.class.getName());
    private static final Map<String, User> sessions = new ConcurrentHashMap<>();
    private static final Map<String, Long> requestCounts = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT = 100; // requests per minute
    private static final int TIMEOUT = 5000; // 5 seconds

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationService authService;
    private final EmployeeService employeeService;

    public ApiHandler(AuthenticationService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // Handle CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (method.equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (path) {
                case "/api/login":
                    if (method.equals("POST")) {
                        handleLogin(exchange);
                    } else {
                        sendError(exchange, 405, "Method Not Allowed");
                    }
                    break;
                case "/api/employees":
                    if (!isAuthenticated(exchange)) {
                        sendError(exchange, 401, "Unauthorized");
                        return;
                    }
                    switch (method) {
                        case "GET":
                            handleGetEmployees(exchange);
                            break;
                        case "POST":
                            handleCreateEmployee(exchange);
                            break;
                        default:
                            sendError(exchange, 405, "Method Not Allowed");
                    }
                    break;
                default:
                    if (path.matches("/api/employees/\\d+")) {
                        if (!isAuthenticated(exchange)) {
                            sendError(exchange, 401, "Unauthorized");
                            return;
                        }
                        Long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                        switch (method) {
                            case "GET":
                                handleGetEmployee(exchange, id);
                                break;
                            case "PUT":
                                handleUpdateEmployee(exchange, id);
                                break;
                            case "DELETE":
                                handleDeleteEmployee(exchange, id);
                                break;
                            default:
                                sendError(exchange, 405, "Method Not Allowed");
                        }
                    } else {
                        sendError(exchange, 404, "Not Found");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private boolean isAuthenticated(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring(7);
        // In a real application, you would validate the JWT token here
        return true;
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> credentials = objectMapper.readValue(requestBody, Map.class);
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (authService.authenticate(username, password)) {
            Optional<User> user = authService.getUserByUsername(username);
            if (user.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("token", username);
                response.put("role", user.get().getRole());
                String jsonResponse = objectMapper.writeValueAsString(response);
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(jsonResponse.getBytes());
                }
            } else {
                sendError(exchange, 401, "Invalid username or password");
            }
        } else {
            sendError(exchange, 401, "Invalid username or password");
        }
    }

    private void handleGetEmployees(HttpExchange exchange) throws IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        String response = objectMapper.writeValueAsString(employees);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleGetEmployee(HttpExchange exchange, Long id) throws IOException {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            String response = objectMapper.writeValueAsString(employee.get());
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            sendError(exchange, 404, "Employee not found");
        }
    }

    private void handleCreateEmployee(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Employee employee = objectMapper.readValue(requestBody, Employee.class);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        String response = objectMapper.writeValueAsString(savedEmployee);
        exchange.sendResponseHeaders(201, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleUpdateEmployee(HttpExchange exchange, Long id) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Employee employee = objectMapper.readValue(requestBody, Employee.class);
        Employee updatedEmployee = employeeService.saveEmployee(employee);
        String response = objectMapper.writeValueAsString(updatedEmployee);
        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleDeleteEmployee(HttpExchange exchange, Long id) throws IOException {
        employeeService.deleteEmployee(id);
        exchange.sendResponseHeaders(204, -1);
    }

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        String response = "{\"error\":\"" + message + "\"}";
        exchange.sendResponseHeaders(code, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
} 