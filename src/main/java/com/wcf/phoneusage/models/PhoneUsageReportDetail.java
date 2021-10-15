package com.wcf.phoneusage.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Month;
import java.util.Map;

@Getter
@Setter
public class PhoneUsageReportDetail {

    private int employeeId;

    private String employeeName;

    private String model;

    private String purchaseDate;

    private Map<Month, Integer> totalMonthlyMins;

    private Map<Month, Float> totalMonthlyData;

}
