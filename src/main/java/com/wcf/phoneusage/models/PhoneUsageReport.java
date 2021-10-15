package com.wcf.phoneusage.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PhoneUsageReport {

    private PhoneUsageReportHeader phoneUsageReportHeader;

    private List<PhoneUsageReportDetail> phoneUsageReportDetails;

}
