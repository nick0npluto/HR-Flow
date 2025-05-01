package com.employee.manager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    private Long payID;

    @Temporal(TemporalType.DATE)
    private Date payDate;

    private Double earnings;
    private Double fedTax;
    private Double fedMed;
    private Double fedSS;
    private Double stateTax;
    private Double retire401k;
    private Double healthCare;

    @ManyToOne
    @JoinColumn(name = "empid")  // foreign key in payroll table
    private Employee employee;
}
