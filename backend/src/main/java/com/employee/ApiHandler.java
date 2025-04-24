package com.employee;

import com.employee.model.Employee;
import com.employee.model.User;
import com.employee.service.AuthenticationService;
import com.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmployeeService employeeService = new EmployeeService();
    private final Map<String, User> activeSessions = new HashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

        // Handle preflight requests
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        try {
            // Handle authentication endpoints
            if (path.equals("/api/login")) {
                handleLogin(exchange);
                return;
            }

            if (path.equals("/api/logout")) {
                handleLogout(exchange);
                return;
            }

            // Check authentication for all other endpoints
            String sessionId = getSessionId(exchange);
            User user = activeSessions.get(sessionId);
            
            if (user == null) {
                sendResponse(exchange, 401, "Unauthorized");
                return;
            }

            // Handle employee endpoints with role-based access control
            if (path.startsWith("/api/employees")) {
                if (path.equals("/api/employees")) {
                    switch (method) {
                        case "GET":
                            handleGetEmployees(exchange, user);
                            break;
                        case "POST":
                            if (AuthenticationService.isAdmin(user)) {
                                handleCreateEmployee(exchange);
                            } else {
                                sendResponse(exchange, 403, "Forbidden");
                            }
                            break;
                        default:
                            sendResponse(exchange, 405, "Method Not Allowed");
                    }
                } else {
                    String[] parts = path.split("/");
                    if (parts.length == 4) {
                        Long id = Long.parseLong(parts[3]);
                        switch (method) {
                            case "GET":
                                handleGetEmployee(exchange, id, user);
                                break;
                            case "PUT":
                                if (AuthenticationService.isAdmin(user)) {
                                    handleUpdateEmployee(exchange, id);
                                } else {
                                    sendResponse(exchange, 403, "Forbidden");
                                }
                                break;
                            case "DELETE":
                                if (AuthenticationService.isAdmin(user)) {
                                    handleDeleteEmployee(exchange, id);
                                } else {
                                    sendResponse(exchange, 403, "Forbidden");
                                }
                                break;
                            default:
                                sendResponse(exchange, 405, "Method Not Allowed");
                        }
                    }
                }
            } else {
                sendResponse(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        String requestBody = readRequestBody(exchange);
        Map<String, String> credentials = objectMapper.readValue(requestBody, Map.class);
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = AuthenticationService.authenticate(username, password);
        if (user != null) {
            String sessionId = java.util.UUID.randomUUID().toString();
            activeSessions.put(sessionId, user);
            
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", sessionId);
            response.put("role", user.getRole());
            
            sendResponse(exchange, 200, objectMapper.writeValueAsString(response));
        } else {
            sendResponse(exchange, 401, "Invalid credentials");
        }
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        String sessionId = getSessionId(exchange);
        if (sessionId != null) {
            activeSessions.remove(sessionId);
        }
        sendResponse(exchange, 200, "Logged out successfully");
    }

    private String getSessionId(HttpExchange exchange) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader != null) {
            for (String cookie : cookieHeader.split(";")) {
                String[] parts = cookie.trim().split("=");
                if (parts.length == 2 && parts[0].equals("sessionId")) {
                    return parts[1];
                }
            }
        }
        return null;
    }

    private void handleGetEmployees(HttpExchange exchange, User user) throws IOException {
        if (AuthenticationService.isAdmin(user)) {
            sendResponse(exchange, 200, objectMapper.writeValueAsString(employeeService.getAllEmployees()));
        } else {
            // For regular employees, only return their own data
            Employee employee = employeeService.getEmployeeByUsername(user.getUsername());
            if (employee != null) {
                sendResponse(exchange, 200, objectMapper.writeValueAsString(employee));
            } else {
                sendResponse(exchange, 404, "Employee not found");
            }
        }
    }

    private void handleGetEmployee(HttpExchange exchange, Long id, User user) throws IOException {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            if (AuthenticationService.isAdmin(user) || 
                (AuthenticationService.isEmployee(user) && employee.getUsername().equals(user.getUsername()))) {
                sendResponse(exchange, 200, objectMapper.writeValueAsString(employee));
            } else {
                sendResponse(exchange, 403, "Forbidden");
            }
        } else {
            sendResponse(exchange, 404, "Employee not found");
        }
    }

    private void handleCreateEmployee(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Employee employee = objectMapper.readValue(requestBody, Employee.class);
        employeeService.createEmployee(employee);
        sendResponse(exchange, 201, objectMapper.writeValueAsString(employee));
    }

    private void handleUpdateEmployee(HttpExchange exchange, Long id) throws IOException {
        String requestBody = readRequestBody(exchange);
        Employee employee = objectMapper.readValue(requestBody, Employee.class);
        employee.setId(id);
        employeeService.updateEmployee(employee);
        sendResponse(exchange, 200, objectMapper.writeValueAsString(employee));
    }

    private void handleDeleteEmployee(HttpExchange exchange, Long id) throws IOException {
        employeeService.deleteEmployee(id);
        sendResponse(exchange, 204, "");
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
} 