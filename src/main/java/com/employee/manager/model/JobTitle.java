package com.employee.manager.model;

import lombok.Data;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Data
@Entity
@Table(name = "job_titles")
public class JobTitle {
    @Id
    @Column(name = "job_title_id")
    private Long jobTitleId;

    @Column(name = "job_title")
    private String jobTitle;

    @OneToMany(mappedBy = "jobTitle")
    @JsonIgnore
    private List<Employee> employees;

    // Explicit getter and setter for jobTitleId
    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
} 