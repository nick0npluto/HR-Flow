package com.employee.manager.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // ✅ Link to employee (One user → one employee)
    @OneToOne
    @JoinColumn(name = "empid", referencedColumnName = "empid")
    private Employee employee;
}
