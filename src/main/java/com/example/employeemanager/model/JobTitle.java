package com.example.employeemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "job_titles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_title_id")
    private Long jobTitleId;

    @Column(name = "job_title")
    private String jobTitle;

    @OneToMany(mappedBy = "jobTitle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;

    // Getters and Setters
    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
} 