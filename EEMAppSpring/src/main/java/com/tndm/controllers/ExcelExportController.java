package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.Problem;
import com.tndm.pojo.RepairHistory;
import com.tndm.services.DeviceService;
import com.tndm.services.ProblemService;
import com.tndm.services.RepairHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class ExcelExportController {

    private static final int TOTAL_COST_COLUMN = 9;
    private static final int REPAIR_TYPE_COLUMN = 6;
    private static final int COST_COLUMN = 7;
    private static final int REPAIR_DATE_COLUMN = 8;

    @Autowired
    private RepairHistoryService repHistoryService;

    @Autowired
    private ProblemService proService;

    @Autowired
    private DeviceService devService;

    @GetMapping("/export-excel")
    public void exportExcel(
            HttpServletResponse response,
            @RequestParam(name = "deviceId", required = false) Integer deviceId,
            @RequestParam(name = "typeId", required = false) Integer typeId) {

        try {
            Map<String, Object> modelData = getReportData(deviceId, typeId);
            List<Problem> problems = (List<Problem>) modelData.get("problems");
            Map<Integer, List<RepairHistory>> repairMap = (Map<Integer, List<RepairHistory>>) modelData.get("repairDetail");
            Map<Integer, BigDecimal> repairTotal = (Map<Integer, BigDecimal>) modelData.get("repairTotal");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=bao_cao_sua_chua.xlsx");

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Báo cáo sự cố");

                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle currencyStyle = createCurrencyStyle(workbook);
                CellStyle dateStyle = createDateStyle(workbook);
                CellStyle mergedCenterStyle = createMergedCenterStyle(workbook);
                CellStyle totalStyle = createTotalStyle(workbook);

                createHeaderRow(sheet, headerStyle);

                int rowNum = 1;
                for (Problem problem : problems) {
                    List<RepairHistory> repairs = repairMap.getOrDefault(problem.getId(), Collections.emptyList());
                    BigDecimal total = repairTotal.getOrDefault(problem.getId(), BigDecimal.ZERO);

                    if (!repairs.isEmpty()) {
                        rowNum = processProblemRow(
                                workbook,
                                sheet,
                                problem,
                                repairs,
                                total,
                                rowNum,
                                currencyStyle,
                                dateStyle,
                                mergedCenterStyle,
                                totalStyle
                        );
                    }
                }

                for (int i = 0; i <= TOTAL_COST_COLUMN; i++) {
                    sheet.autoSizeColumn(i);
                    int currentWidth = sheet.getColumnWidth(i);
                    sheet.setColumnWidth(i, Math.min(currentWidth + 1000, 255 * 256));
                }

                workbook.write(response.getOutputStream());
            }
        } catch (Exception e) {
            handleExportError(response, e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0\" VND\""));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createMergedCenterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0\" VND\""));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private int processProblemRow(Workbook workbook,
            Sheet sheet,
            Problem problem,
            List<RepairHistory> repairs,
            BigDecimal total,
            int rowNum,
            CellStyle currencyStyle,
            CellStyle dateStyle,
            CellStyle mergedCenterStyle,
            CellStyle totalStyle) {

        int startRow = rowNum;

        for (RepairHistory repair : repairs) {
            Row row = sheet.createRow(rowNum++);
            addCommonProblemInfo(row, problem, dateStyle, mergedCenterStyle, startRow == rowNum - 1);
            addRepairDetails(row, repair, currencyStyle, mergedCenterStyle);
        }

        if (!repairs.isEmpty()) {
            addTotalRow(sheet, startRow, rowNum - 1, total, totalStyle);
        }

        if (startRow < rowNum - 1) {
            mergeCommonCells(sheet, startRow, rowNum - 1, mergedCenterStyle);
        }

        return rowNum;
    }

    private void addTotalRow(Sheet sheet,
            int startRow,
            int endRow,
            BigDecimal total,
            CellStyle totalStyle) {

        Row row = sheet.getRow(startRow);
        if (row == null) {
            row = sheet.createRow(startRow);
        }

        Cell totalCell = row.createCell(TOTAL_COST_COLUMN);
        totalCell.setCellValue(total != null ? total.doubleValue() : 0);
        totalCell.setCellStyle(totalStyle);

        if (startRow < endRow) {
            sheet.addMergedRegion(new CellRangeAddress(
                    startRow,
                    endRow,
                    TOTAL_COST_COLUMN,
                    TOTAL_COST_COLUMN
            ));
        }
    }

    private void addCommonProblemInfo(Row row,
            Problem problem,
            CellStyle dateStyle,
            CellStyle mergedCenterStyle,
            boolean writeValues) {

        if (writeValues) {
            safeCreateCell(row, 0,
                    problem.getDeviceId() != null ? problem.getDeviceId().getName() : "N/A",
                    mergedCenterStyle);

            safeCreateCell(row, 1,
                    (problem.getDeviceId() != null && problem.getDeviceId().getFacilityId() != null)
                    ? problem.getDeviceId().getFacilityId().getName()
                    : "N/A",
                    mergedCenterStyle);

            safeCreateCell(row, 2, problem.getDescription(), mergedCenterStyle);

            safeCreateCell(row, 3,
                    (problem.getProblemStatus()!= null) ? problem.getProblemStatus() : "N/A",
                    mergedCenterStyle);

            Cell dateCell = row.createCell(4);
            dateCell.setCellStyle(dateStyle);
            if (problem.getHappenedDate() != null) {
                dateCell.setCellValue(problem.getHappenedDate());
            } else {
                dateCell.setCellValue("N/A");
            }
        }
    }

    private void addRepairDetails(Row row,
            RepairHistory repair,
            CellStyle currencyStyle,
            CellStyle mergedCenterStyle) {

        String techName = "N/A";
        if (repair.getTechnicianId() != null
                && repair.getTechnicianId().getUser() != null) {
            techName = repair.getTechnicianId().getUser().getFirstName() + " "
                    + repair.getTechnicianId().getUser().getLastName();
        }
        safeCreateCell(row, 5, techName, mergedCenterStyle);

        String repairType = "N/A";
        if (repair.getTypeId() != null && repair.getTypeId().getName() != null) {
            repairType = repair.getTypeId().getName();
        }
        safeCreateCell(row, REPAIR_TYPE_COLUMN, repairType, mergedCenterStyle);

        Cell expenseCell = row.createCell(COST_COLUMN);
        expenseCell.setCellValue(repair.getExpense() != null ? repair.getExpense().doubleValue() : 0);
        expenseCell.setCellStyle(currencyStyle);

        Cell dateCell = row.createCell(REPAIR_DATE_COLUMN);
        dateCell.setCellValue(formatRepairDate(repair));
        dateCell.setCellStyle(mergedCenterStyle);
    }

    private void createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "Thiết bị", "Cơ sở", "Mô tả sự cố", "Trạng thái",
            "Ngày xảy ra", "Kỹ thuật viên", "Loại sửa chữa",
            "Chi phí", "Thời gian sửa", "Tổng chi phí"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void mergeCommonCells(Sheet sheet,
            int startRow,
            int endRow,
            CellStyle mergedStyle) {

        for (int i = 0; i < 5; i++) {
            CellRangeAddress region = new CellRangeAddress(startRow, endRow, i, i);
            if (!isRegionOverlap(sheet, region)) {
                sheet.addMergedRegion(region);
            }
        }
    }

    private boolean isRegionOverlap(Sheet sheet, CellRangeAddress newRegion) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress existingRegion = sheet.getMergedRegion(i);
            if (existingRegion.intersects(newRegion)) {
                return true;
            }
        }
        return false;
    }

    private void safeCreateCell(Row row, int column, String value, CellStyle style) {
        String displayValue = (value == null || value.isEmpty()) ? "N/A" : value;
        Cell cell = row.createCell(column);
        cell.setCellValue(displayValue);
        cell.setCellStyle(style);
    }

    private String formatRepairDate(RepairHistory repair) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (repair.getStartDate() == null) {
            return "N/A";
        }
        return sdf.format(repair.getStartDate());
    }

    private void handleExportError(HttpServletResponse response, Exception e) {
        try {
            response.reset();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Lỗi xuất file: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Map<String, Object> getReportData(Integer deviceId, Integer typeId) {
        Map<String, Object> model = new HashMap<>();

        List<Problem> problems = fetchProblems(deviceId, typeId).stream()
                .filter(p -> p.getProblemStatus() != null && "Đã sửa chữa".equals(p.getProblemStatus()))
                .collect(Collectors.toList());

        Map<Integer, List<RepairHistory>> repairMap = problems.stream()
                .collect(Collectors.toMap(
                        Problem::getId,
                        p -> repHistoryService.getRepairHistoriesByProblemId(p.getId())
                ));

        Map<Integer, BigDecimal> repairTotal = calculateTotalCosts(repairMap);

        model.put("problems", problems);
        model.put("repairDetail", repairMap);
        model.put("repairTotal", repairTotal);

        return model;
    }

    private List<Problem> fetchProblems(Integer deviceId, Integer typeId) {
        if (deviceId != null) {
            return proService.getProblemsByDeviceIds(Collections.singletonList(deviceId));
        }

        if (typeId != null) {
            List<Device> devices = devService.getDevicesByTypeId(typeId);
            List<Integer> deviceIds = devices.stream()
                    .map(Device::getId)
                    .collect(Collectors.toList());
            return proService.getProblemsByDeviceIds(deviceIds);
        }

        return proService.getProblemsByDeviceIds(
                devService.getAllDevices().stream()
                        .map(Device::getId)
                        .collect(Collectors.toList())
        );
    }

    private Map<Integer, BigDecimal> calculateTotalCosts(Map<Integer, List<RepairHistory>> repairMap) {
        return repairMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(RepairHistory::getExpense)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ));
    }
}
