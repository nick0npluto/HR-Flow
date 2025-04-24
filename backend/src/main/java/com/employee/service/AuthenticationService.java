package com.employee.service;

import com.employee.model.User;
import com.employee.repository.UserRepository;
import java.util.Optional;

public class AuthenticationService {
    private static final UserRepository userRepository = new UserRepository();

    public static User authenticate(String username, String password) {
        System.out.println("Attempting to authenticate user: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found in database: " + user.getUsername());
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Provided password: " + password);
            
            if (user.getPassword().equals(password)) {
                System.out.println("Password match, authentication successful");
                return user;
            } else {
                System.out.println("Password mismatch");
            }
        } else {
            System.out.println("User not found in database");
        }
        return null;
    }

    public static boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    public static boolean isEmployee(User user) {
        return user != null && "EMPLOYEE".equals(user.getRole());
    }
} 