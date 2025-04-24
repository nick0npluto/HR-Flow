package com.employee;

import com.employee.ApiHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Create context for the API
        server.createContext("/api", new ApiHandler());
        
        // Start the server
        server.start();
        System.out.println("Server started on port 8080");
    }
} 