package com.employee.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()

                // Public: login
                .antMatchers("/api/auth/**").permitAll()

                // EMPLOYEE: view their own payroll
                .antMatchers("/api/payroll/me/**").hasRole("EMPLOYEE")

                // ADMIN: view all payroll + reports
                .antMatchers("/api/payroll/**").hasRole("ADMIN")
                .antMatchers("/api/reports/**").hasRole("ADMIN")

                // EMPLOYEE and ADMIN: view employee info
                .antMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("ADMIN", "EMPLOYEE")

                // ADMIN only: create, update, delete employees
                .antMatchers(HttpMethod.POST, "/api/employees/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")

                // fallback for all other routes
                .anyRequest().authenticated()

            .and()
            .httpBasic();
    }
}
