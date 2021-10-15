package com.wcf.phoneusage.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneUsageReportHeader {

    private String generatedDate;

    private int totalPhones;

    private int totalMins;

    private float totalData;

    private int avgMins;

    private float avgData;

}
