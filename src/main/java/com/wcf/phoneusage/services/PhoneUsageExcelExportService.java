package com.wcf.phoneusage.services;

import com.wcf.phoneusage.models.PhoneUsageReport;
import com.wcf.phoneusage.models.PhoneUsageReportDetail;
import com.wcf.phoneusage.models.PhoneUsageReportHeader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class PhoneUsageExcelExportService extends ExcelExportService {

    public void generateExcel(PhoneUsageReport phoneUsageReport) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Cell Phone Usage");
        for (int i = 0; i < 28; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        generateHeader(sheet, workbook, phoneUsageReport.getPhoneUsageReportHeader());
        generateDetails(sheet, workbook, phoneUsageReport.getPhoneUsageReportDetails());

        Path currDir = Paths.get("src", "main", "resources", "reports", phoneUsageReport.getPhoneUsageReportHeader().getGeneratedDate());
        String path = currDir.toAbsolutePath().toString();
        String fileLocation = path + ".xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();

    }

    private void generateHeader(Sheet sheet, XSSFWorkbook workbook, PhoneUsageReportHeader phoneUsageReportHeader) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle contentStyle = createContentStyle(workbook);

        createCell(header, 0, headerStyle, "Generated Date");
        createCell(header, 1, headerStyle, "Number of phones");
        createCell(header, 2, headerStyle, "Total minutes usage");
        createCell(header, 3, headerStyle, "Total data usage");
        createCell(header, 4, headerStyle, "Average minutes used");
        createCell(header, 5, headerStyle, "Average data used");

        Row headerContent = sheet.createRow(1);
        createCell(headerContent, 0, contentStyle, phoneUsageReportHeader.getGeneratedDate());
        createCell(headerContent, 1, contentStyle, phoneUsageReportHeader.getTotalPhones());
        createCell(headerContent, 2, contentStyle, phoneUsageReportHeader.getTotalMins());
        createCell(headerContent, 3, contentStyle, phoneUsageReportHeader.getTotalData());
        createCell(headerContent, 4, contentStyle, phoneUsageReportHeader.getAvgMins());
        createCell(headerContent, 5, contentStyle, phoneUsageReportHeader.getAvgData());
    }

    private void generateDetails(Sheet sheet, XSSFWorkbook workbook, List<PhoneUsageReportDetail> phoneUsageReportDetailList) {
        Row header = sheet.createRow(5);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle contentStyle = createContentStyle(workbook);

        createCell(header, 0, headerStyle, "Employee ID");
        createCell(header, 1, headerStyle, "Employee name");
        createCell(header, 2, headerStyle, "Phone model");
        createCell(header, 3, headerStyle, "Purchase Date");
        int monthColIdx = 4;
        Month[] months = Month.values();
        for (Month month : months) {
            createCell(header, monthColIdx, headerStyle, month.name() + " - Mins");
            createCell(header, monthColIdx + 1, headerStyle, month.name() + " - Data");
            monthColIdx = monthColIdx + 2;
        }

        for (int i = 0; i < phoneUsageReportDetailList.size(); i++) {
            Row detailContent = sheet.createRow(i + 6);
            PhoneUsageReportDetail detail = phoneUsageReportDetailList.get(i);
            createCell(detailContent, 0, contentStyle, detail.getEmployeeId());
            createCell(detailContent, 1, contentStyle, detail.getEmployeeName());
            createCell(detailContent, 2, contentStyle, detail.getModel());
            createCell(
                    detailContent,
                    3,
                    contentStyle,
                    LocalDate.parse(detail.getPurchaseDate(), DateTimeFormatter.ofPattern("yyyyMMdd"))
                            .format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
            );

            monthColIdx = 4;
            Map<Month, Integer> monthlyMinsUsage = detail.getTotalMonthlyMins();
            Map<Month, Float> monthlyDataUsage = detail.getTotalMonthlyData();
            for (Month month : months) {
                createCell(detailContent, monthColIdx, contentStyle, monthlyMinsUsage.getOrDefault(month, 0));
                createCell(detailContent, monthColIdx + 1, contentStyle, monthlyDataUsage.getOrDefault(month, 0f));
                monthColIdx = monthColIdx + 2;
            }
        }
    }

}
