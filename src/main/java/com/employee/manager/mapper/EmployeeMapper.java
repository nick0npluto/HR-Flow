package com.employee.manager.mapper;

import com.employee.manager.dto.EmployeeDTO;
import com.employee.manager.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    
    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmpid(employee.getEmpid());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setSsn(employee.getSsn());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setSalary(employee.getSalary());
        
        if (employee.getJobTitle() != null) {
            dto.setJobTitle(employee.getJobTitle().getJobTitle());
            dto.setJobTitleId(employee.getJobTitle().getJobTitleId());
        }
        
        if (employee.getDivision() != null) {
            dto.setDivisionName(employee.getDivision().getName());
            dto.setDivisionId(employee.getDivision().getId());
        }
        
        if (employee.getDepartment() != null) {
            dto.setDepartmentName(employee.getDepartment().getName());
            dto.setDepartmentId(employee.getDepartment().getId());
        }
        
        return dto;
    }

    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmpid(dto.getEmpid());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setSsn(dto.getSsn());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setSalary(dto.getSalary());
        
        return employee;
    }
} 