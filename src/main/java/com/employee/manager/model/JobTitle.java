package com.employee.manager.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "job_titles")
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_title_id")
    private Long id;

    @Column(name = "job_title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "jobTitle")
    private List<Employee> employees;
} 