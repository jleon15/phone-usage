package com.wcf.phoneusage.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cell_phones", schema = "phone_usage")
public class CellPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column
    private String model;

    @OneToMany(mappedBy = "cellPhone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CellPhoneUsageByMonth> phoneUsages = new HashSet<>();

}
