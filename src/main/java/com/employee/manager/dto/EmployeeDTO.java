package com.employee.manager.dto;

import lombok.Data;
import java.util.Date;

@Data
public class EmployeeDTO {
    private Long empid;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private Date dateOfBirth;
    private String jobTitle;
    private Long jobTitleId;
    private String divisionName;
    private Long divisionId;
    private String departmentName;
    private Long departmentId;
    private Double salary;
} 