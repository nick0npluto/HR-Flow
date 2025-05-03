package com.employee.manager.model;

import lombok.Data;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "department_name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Employee> employees;

    public void setId(Long id) {
        this.id = id;
    }
} 