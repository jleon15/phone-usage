package com.wcf.phoneusage;

import com.wcf.phoneusage.services.PhoneUsageExcelExportService;
import com.wcf.phoneusage.services.IPhoneUsageReportService;
import com.wcf.phoneusage.services.PhoneUsageReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class PhoneUsageApplication {

	public static void main(String[] args) throws IOException {
		ApplicationContext applicationContext = SpringApplication.run(PhoneUsageApplication.class, args);
		IPhoneUsageReportService phoneUsageReportService = applicationContext.getBean(PhoneUsageReportService.class);
		PhoneUsageExcelExportService excelExportService = applicationContext.getBean(PhoneUsageExcelExportService.class);

		excelExportService.generateExcel(phoneUsageReportService.generateReportData());
	}

}
