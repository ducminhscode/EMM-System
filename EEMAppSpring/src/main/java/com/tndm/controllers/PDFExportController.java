/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tndm.pojo.RepairHistory;
import com.tndm.services.RepairHistoryService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author ADMIN
 */
@Controller
public class PDFExportController {

    @Autowired
    private RepairHistoryService repHistoryService;

    @GetMapping("/repair-report")
    public void generateRepairReport(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=repair_report.pdf");

        try {
            OutputStream out = response.getOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Ngang cho nhiều cột
            PdfWriter.getInstance(document, out);
            document.open();

            // Font hỗ trợ tiếng Việt
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf");
            BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null);
            Font font = new Font(baseFont, 10);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            // Tiêu đề
            Paragraph title = new Paragraph("BÁO CÁO SỬA CHỮA THIẾT BỊ", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Tạo bảng
            PdfPTable table = new PdfPTable(8); // 8 cột tương ứng các trường
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 2f, 3f, 3f, 3f, 3f, 3f, 3f});

            // Tiêu đề cột
            String[] headers = {
                "ID", "Chi phí", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc",
                "Sự cố", "Loại sửa chữa", "Kỹ thuật viên"
            };

            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, boldFont));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(headerCell);
            }

            // Danh sách dữ liệu (giả lập)
            List<RepairHistory> repairs = this.repHistoryService.getAllRepairHistorires();

            for (RepairHistory r : repairs) {
                table.addCell(new Phrase(String.valueOf(r.getId()), font));
                table.addCell(new Phrase(r.getExpense().toString(), font));
                table.addCell(new Phrase(r.getDescription() != null ? r.getDescription() : "", font));
                table.addCell(new Phrase(r.getStartDate().toString(), font));
                table.addCell(new Phrase(r.getEndDate().toString(), font));
                table.addCell(new Phrase(r.getProblemId() != null ? r.getProblemId().getDescription() : "", font));
                table.addCell(new Phrase(r.getTypeId() != null ? r.getTypeId().getName() : "", font));
                table.addCell(new Phrase(r.getTechnicianId() != null ? r.getTechnicianId().getUser().getFirstName() : "", font));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
