package com.vinsys.hrms.idp.reports.helper;

import com.vinsys.hrms.constants.ELogo;
import com.vinsys.hrms.directonboard.util.IColumnPositionConstants;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.progress.util.ExcelFileIndexEnum;
import com.vinsys.hrms.logo.entity.Logo;
import com.vinsys.hrms.logo.service.LogoService;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;

public class ExcelHelper {

    public static InputStream getInputStreamFromPath(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found at path: " + filePath);
        }
        return new FileInputStream(file);
    }

    public static Cell createCell(Row row, int index, String value, CellStyle style) {
        Cell cell0 = row.createCell(index);
        cell0.setCellValue(value);
        cell0.setCellStyle(style);
        return cell0;
    }

    public static Cell createCell(Row row, int index, Integer value, CellStyle style) {
        Cell cell0 = row.createCell(index);
        cell0.setCellValue(value);
        cell0.setCellStyle(style);
        return cell0;
    }

    public static Cell createCell(Row row, int index, Long value, CellStyle style) {
        Cell cell0 = row.createCell(index);
        cell0.setCellValue(value);
        cell0.setCellStyle(style);
        return cell0;
    }

    public static Cell createCell(Row row, int index, Double value, CellStyle style) {
        Cell cell0 = row.createCell(index);
        cell0.setCellValue(value);
        cell0.setCellStyle(style);
        return cell0;
    }

    public static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        return headerStyle;
    }

    public static CellStyle getTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.RIGHT);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    public static CellStyle getDateStyle(Workbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        Font dateFont = workbook.createFont();
        dateFont.setBold(false);
        dateStyle.setFont(dateFont);
        dateStyle.setAlignment(HorizontalAlignment.RIGHT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return dateStyle;
    }

    public static Sheet createInitialSheet(Workbook workbook, String title, String headers[], LogoService logoService) throws IOException {
        Sheet sheet = workbook.createSheet(title);
        sheet.setDisplayGridlines(false);
        CreationHelper helper = workbook.getCreationHelper();
        int totalCols = headers.length;
        for (int i = 0; i < 4; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < totalCols; j++) {
                row.createCell(j);
            }
        }

        Logo logo = logoService.getConfig(ELogo.LOGO.name());
        InputStream logoStream = ExcelHelper.getInputStreamFromPath(logo.getValue());
        byte[] logoBytes = IOUtils.toByteArray(logoStream);
        int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_JPEG);
        logoStream.close();

        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        anchor.setCol2(2);
        anchor.setRow2(4);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
        drawing.createPicture(anchor, pictureIdx);

        int titleRowIndex = 1;
        int titleStartCol = totalCols / 2 - 1;
        int titleEndCol = Math.min(totalCols - 1, titleStartCol + 2);
        sheet.addMergedRegion(new CellRangeAddress(titleRowIndex, titleRowIndex, titleStartCol, titleEndCol));

        Row titleRow = sheet.getRow(titleRowIndex);
        Cell titleCell = titleRow.createCell(titleStartCol);
        titleCell.setCellValue(title);

        titleCell.setCellStyle(ExcelHelper.getTitleStyle(workbook));

        Cell dateCell = titleRow.createCell(totalCols - 1);
        dateCell.setCellValue("Date: " + LocalDate.now());

        dateCell.setCellStyle(ExcelHelper.getDateStyle(workbook));

        int headerRowIndex = 4;
        Row headerRow = sheet.createRow(headerRowIndex);

        CellStyle headerStyle = ExcelHelper.getHeaderStyle(workbook);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return sheet;
    }

    public static int getNumber(Row row, int rowCounter, ExcelFileIndexEnum excelFileIndexEnum) throws HRMSException {
        try {
            return (int) row.getCell(excelFileIndexEnum.getIndex()).getNumericCellValue();
        } catch (Exception e) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
                    excelFileIndexEnum.getName() + " At Row " + rowCounter);
        }
    }

    public static String getString(Row row, int rowCounter, ExcelFileIndexEnum excelFileIndexEnum) throws HRMSException {
        try {
            return row.getCell(excelFileIndexEnum.getIndex()).getStringCellValue().trim();
        } catch (Exception e) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
                    excelFileIndexEnum.getName() + " At Row " + rowCounter);
        }
    }

    public static Date getDate(Row row, int rowCounter, ExcelFileIndexEnum excelFileIndexEnum) throws HRMSException {
        try {
            return row.getCell(excelFileIndexEnum.getIndex()).getDateCellValue();
        } catch (Exception e) {
            throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
                    excelFileIndexEnum.getName() + " At Row " + rowCounter);
        }
    }
}
