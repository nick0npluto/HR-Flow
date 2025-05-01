package com.employee.manager.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "divisions")
public class Division {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "division_id")
    private Long id;

    @Column(name = "division_name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "division")
    private List<Employee> employees;
} 