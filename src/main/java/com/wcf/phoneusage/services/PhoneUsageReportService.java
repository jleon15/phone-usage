package com.wcf.phoneusage.services;

import com.wcf.phoneusage.entities.CellPhone;
import com.wcf.phoneusage.entities.CellPhoneUsageByMonth;
import com.wcf.phoneusage.models.PhoneUsageReport;
import com.wcf.phoneusage.models.PhoneUsageReportDetail;
import com.wcf.phoneusage.models.PhoneUsageReportHeader;
import com.wcf.phoneusage.repositories.CellPhoneRepository;
import com.wcf.phoneusage.repositories.CellPhoneUsageByMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhoneUsageReportService implements IPhoneUsageReportService {

    @Value("${REPORT_YEAR}")
    private Integer year;

    private CellPhoneRepository cellPhoneRepository;

    private CellPhoneUsageByMonthRepository cellPhoneUsageByMonthRepository;

    @Autowired
    public PhoneUsageReportService(
            CellPhoneRepository cellPhoneRepository,
            CellPhoneUsageByMonthRepository cellPhoneUsageByMonthRepository
    ) {
        this.cellPhoneRepository = cellPhoneRepository;
        this.cellPhoneUsageByMonthRepository = cellPhoneUsageByMonthRepository;
    }

    @Override
    @Transactional
    public PhoneUsageReport generateReportData() {
        List<CellPhone> cellPhoneList = cellPhoneRepository.findAll();
        List<CellPhoneUsageByMonth> cellPhoneUsageByMonths = cellPhoneUsageByMonthRepository.findAll();

        PhoneUsageReport phoneUsageReport = new PhoneUsageReport();
        phoneUsageReport.setPhoneUsageReportHeader(generateReportHeader(cellPhoneList, cellPhoneUsageByMonths));
        phoneUsageReport.setPhoneUsageReportDetails(generateReportDetails(cellPhoneUsageByMonths));

        return phoneUsageReport;
    }

    private PhoneUsageReportHeader generateReportHeader(
            List<CellPhone> cellPhoneList,
            List<CellPhoneUsageByMonth> cellPhoneUsageByMonthList
    ) {
        int totalPhones = cellPhoneList.size();
        int totalMins = cellPhoneUsageByMonthList.stream()
                .filter(phoneUsage ->
                        LocalDate.parse(phoneUsage.getDate(), DateTimeFormatter.ofPattern("M/d/yyyy")).getYear() == year
                ).map(CellPhoneUsageByMonth::getTotalMinutes)
                .reduce(0, Integer::sum);
        float totalData = cellPhoneUsageByMonthList.stream()
                .filter(phoneUsage ->
                        LocalDate.parse(phoneUsage.getDate(), DateTimeFormatter.ofPattern("M/d/yyyy")).getYear() == year
                ).map(CellPhoneUsageByMonth::getTotalData)
                .reduce(0f, Float::sum);

        PhoneUsageReportHeader phoneUsageReportHeader = new PhoneUsageReportHeader();
        phoneUsageReportHeader.setGeneratedDate(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy h m a"))
        );
        phoneUsageReportHeader.setTotalPhones(totalPhones);
        phoneUsageReportHeader.setTotalMins(totalMins);
        phoneUsageReportHeader.setTotalData(totalData);
        phoneUsageReportHeader.setAvgMins(totalMins / totalPhones);
        phoneUsageReportHeader.setAvgData(totalData / totalPhones);

        return phoneUsageReportHeader;
    }

    private List<PhoneUsageReportDetail> generateReportDetails(
            List<CellPhoneUsageByMonth> cellPhoneUsageByMonthList
    ) {
        Map<CellPhone, List<CellPhoneUsageByMonth>> phoneUsagesPerEmployee = cellPhoneUsageByMonthList.stream()
                .filter(phoneUsage ->
                        LocalDate.parse(phoneUsage.getDate(), DateTimeFormatter.ofPattern("M/d/yyyy")).getYear() == year
                ).collect(
                        Collectors.groupingBy(CellPhoneUsageByMonth::getCellPhone)
                );

        List<PhoneUsageReportDetail> phoneUsageReportDetails = new ArrayList<>();

        phoneUsagesPerEmployee.forEach((employeePhone, records) ->
                phoneUsageReportDetails.add(generateReportDetail(employeePhone, records))
        );

        return phoneUsageReportDetails;
    }

    private PhoneUsageReportDetail generateReportDetail(CellPhone employeePhone, List<CellPhoneUsageByMonth> records) {
        PhoneUsageReportDetail phoneUsageReportDetail = new PhoneUsageReportDetail();
        phoneUsageReportDetail.setEmployeeId(employeePhone.getEmployeeId());
        phoneUsageReportDetail.setEmployeeName(employeePhone.getEmployeeName());
        phoneUsageReportDetail.setModel(employeePhone.getModel());
        phoneUsageReportDetail.setPurchaseDate(employeePhone.getPurchaseDate());

        Map<Month, List<CellPhoneUsageByMonth>> totalRecordsPerMonth = records.stream()
                .collect(Collectors.groupingBy(
                        record -> LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("M/d/yyyy")).getMonth()
                ));

        Map<Month, Integer> totalMinsPerMonth = new HashMap<>();
        Map<Month, Float> totalDataPerMonth = new HashMap<>();

        totalRecordsPerMonth.forEach((month, recordsPerMonth) -> {
            totalMinsPerMonth.put(
                    month,
                    recordsPerMonth.stream()
                            .map(CellPhoneUsageByMonth::getTotalMinutes)
                            .reduce(0, Integer::sum)
            );

            totalDataPerMonth.put(
                    month,
                    recordsPerMonth.stream()
                            .map(CellPhoneUsageByMonth::getTotalData)
                            .reduce(0f, Float::sum)
            );
        });

        phoneUsageReportDetail.setTotalMonthlyMins(totalMinsPerMonth);
        phoneUsageReportDetail.setTotalMonthlyData(totalDataPerMonth);

        return phoneUsageReportDetail;
    }

}
