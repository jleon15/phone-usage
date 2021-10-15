package com.wcf.phoneusage.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "phone_usage_by_month", schema = "phone_usage")
public class CellPhoneUsageByMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String date;

    @Column(name = "total_minutes")
    private Integer totalMinutes;

    @Column(name = "total_data")
    private Float totalData;

    @ManyToOne(fetch = FetchType.LAZY)
    private CellPhone cellPhone;

}
