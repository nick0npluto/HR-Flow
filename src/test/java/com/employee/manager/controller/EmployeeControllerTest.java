package com.employee.manager.controller;

import com.employee.manager.model.Employee;
import com.employee.manager.service.EmployeeService;
import com.employee.manager.dto.EmployeeDTO;
import com.employee.manager.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee testEmployee;
    private EmployeeDTO testEmployeeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create test employee
        testEmployee = new Employee();
        testEmployee.setEmpid(1L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setSsn("123-45-6789");
        testEmployee.setDateOfBirth(new Date());
        testEmployee.setSalary(50000.0);

        // Create test DTO
        testEmployeeDTO = new EmployeeDTO();
        testEmployeeDTO.setEmpid(1L);
        testEmployeeDTO.setFirstName("John");
        testEmployeeDTO.setLastName("Doe");
        testEmployeeDTO.setEmail("john.doe@example.com");
        testEmployeeDTO.setSsn("123-45-6789");
        testEmployeeDTO.setDateOfBirth(new Date());
        testEmployeeDTO.setSalary(50000.0);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeService.getAllEmployees()).thenReturn(employees);
        when(employeeMapper.toDTO(any(Employee.class))).thenReturn(testEmployeeDTO);

        // Act
        List<EmployeeDTO> result = employeeController.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEmployeeDTO.getEmpid(), result.get(0).getEmpid());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);
        when(employeeMapper.toDTO(testEmployee)).thenReturn(testEmployeeDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(testEmployeeDTO.getEmpid(), response.getBody().getEmpid());
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.getEmployeeById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void searchEmployees_ShouldReturnMatchingEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(testEmployee);
        when(employeeService.searchEmployeesFlexible("John", "Doe", null, null, null))
            .thenReturn(employees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.searchEmployees(
            "John", "Doe", null, null, null
        );

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateEmployee_WhenEmployeeExists_ShouldUpdateAndReturnEmployee() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);
        when(employeeMapper.toEntity(testEmployeeDTO)).thenReturn(testEmployee);
        when(employeeService.updateEmployee(1L, testEmployee)).thenReturn(testEmployee);
        when(employeeMapper.toDTO(testEmployee)).thenReturn(testEmployeeDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.updateEmployee(1L, testEmployeeDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(testEmployeeDTO.getEmpid(), response.getBody().getEmpid());
    }

    @Test
    void updateEmployee_WhenEmployeeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.updateEmployee(1L, testEmployeeDTO);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteEmployee_WhenEmployeeExists_ShouldDeleteAndReturnOk() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployee);
        doNothing().when(employeeService).deleteEmployee(1L);

        // Act
        ResponseEntity<Void> response = employeeController.deleteEmployee(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_WhenEmployeeDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        // Act
        ResponseEntity<Void> response = employeeController.deleteEmployee(1L);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(employeeService, never()).deleteEmployee(1L);
    }
} 