package com.employee.manager.model;

import lombok.Data;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empid")
    private Long empid;

    @Column(name = "Fname", nullable = false)
    private String firstName;

    @Column(name = "Lname", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String ssn;

    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "job_title_id")
    @JsonIgnore
    private JobTitle jobTitle;

    @ManyToOne
    @JoinColumn(name = "division_id")
    @JsonIgnore
    private Division division;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @Column(name = "salary")
    private Double salary;

    // Transient fields for flattened JSON
    @Transient
    private Long jobTitleId;

    @Transient
    private Long divisionId;

    // Explicit getter and setter for empid
    public Long getEmpid() {
        return empid;
    }

    public void setEmpid(Long empid) {
        this.empid = empid;
    }

    // Method to expose job title information
    public String getJobTitleName() {
        return jobTitle != null ? jobTitle.getJobTitle() : null;
    }
} 