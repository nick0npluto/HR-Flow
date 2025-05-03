package com.employee.manager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @Column(name = "empid")
    private Long empid;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "pay_date")
    @Temporal(TemporalType.DATE)
    private Date payDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "empid")
    private Employee employee;
}
