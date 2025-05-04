package com.employee.manager.controller;

import com.employee.manager.dto.UserDTO;
import com.employee.manager.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // Manual login using Spring Security
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Login successful. Role: " + authentication.getAuthorities());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Logout by invalidating session
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // Works if session-based
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully.");
    }

    // Return currently logged-in user's username
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(a -> a.getAuthority().replace("ROLE_", ""))
            .orElse("");
        return ResponseEntity.ok(new UserDTO(authentication.getName(), role));
    }
}
