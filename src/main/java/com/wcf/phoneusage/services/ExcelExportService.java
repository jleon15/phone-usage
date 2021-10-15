package com.wcf.phoneusage.services;

import org.apache.poi.ss.usermodel.*;

public abstract class ExcelExportService {

    protected CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setFontName("Arial");
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;
    }

    protected CellStyle createContentStyle(Workbook workbook) {
        CellStyle contentStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontName("Arial");
        contentStyle.setFont(font);
        contentStyle.setAlignment(HorizontalAlignment.CENTER);

        return contentStyle;
    }

    protected void createCell(Row row, int column, CellStyle cellStyle, String value) {
        Cell headerCell = row.createCell(column);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    protected void createCell(Row row, int column, CellStyle cellStyle, Integer value) {
        Cell headerCell = row.createCell(column);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

    protected void createCell(Row row, int column, CellStyle cellStyle, Float value) {
        Cell headerCell = row.createCell(column);
        headerCell.setCellValue(value);
        headerCell.setCellStyle(cellStyle);
    }

}
