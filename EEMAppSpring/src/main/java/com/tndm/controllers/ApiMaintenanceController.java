/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.MaintenanceAssignmentService;
import com.tndm.services.MaintenanceScheduleService;
import com.tndm.services.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiMaintenanceController {

    @Autowired
    private MaintenanceScheduleService mainScheduleService;

    @Autowired
    private MaintenanceAssignmentService mainAssignService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private DeviceService devService;

    @GetMapping("/maintenance/technician/{technicianId}")
    @CrossOrigin
    public ResponseEntity<?> getMaintenanceByTechnicianId(@PathVariable("technicianId") int id, @RequestParam("page") String page) {
        List<MaintenanceSchedule> mainList = this.mainScheduleService.findSchedulesToNotifyToTechnician(id, page);
        List<Map<String, Object>> listData = new ArrayList<>();

        for (MaintenanceSchedule ms : mainList) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", ms.getId());
            data.put("description", ms.getDescription());
            data.put("title", ms.getTitle());
            data.put("maintenanceStatus", ms.getMaintenanceStatus());
            data.put("deviceName", ms.getDeviceId().getName());
            data.put("startDate", ms.getStartDate());
            data.put("endDate", ms.getEndDate());
            data.put("isCap", this.mainAssignService.getAssignmentByTechnicianIdAndMaintenanceId(id, ms.getId()).getIsCap());
            listData.add(data);
        }
        return ResponseEntity.ok(listData);
    }

    @PatchMapping("secure/maintenance/{maintenanceId}")
    @CrossOrigin
    public ResponseEntity<?> updateMaintenance(@PathVariable("maintenanceId") int maintenanceId,
            Principal principal) {

        try {
            String username = principal.getName();
            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }
            MaintenanceSchedule mainSchedule = this.mainScheduleService.getMaintenanceScheduleById(maintenanceId);
            if (!mainSchedule.getMaintenanceStatus().equals("Ngừng bảo trì")) {
                MaintenanceAssignment mainAssign = this.mainAssignService.getAssignmentByTechnicianIdAndMaintenanceId(existingUser.getId(), maintenanceId);

                if (mainAssign.getIsCap()) {
                    Device d = this.devService.getDeviceById(mainSchedule.getDeviceId().getId());
                    d.setDeviceStatus("Bảo trì");
                    this.devService.addOrUpdateDevice(d);
                    mainSchedule.setMaintenanceStatus("Đang bảo trì");
                    this.mainScheduleService.addOrUpdateMaintenanceSchedule(mainSchedule);
                    return ResponseEntity.ok().body("Cập nhật thành công");
                } else {
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Bạn không có quyền cập nhật thông tin bảo trì");

                }
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Bảo trì này đã ngưng hoạt động");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @PatchMapping("secure/maintenance/technician/{maintenanceId}")
    @CrossOrigin
    public ResponseEntity<?> updateMaintenance(@PathVariable("maintenanceId") int maintenanceId,
            Principal principal,
            @RequestParam Map<String, String> params) {

        try {
            String username = principal.getName();
            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }
            MaintenanceSchedule mainSchedule = this.mainScheduleService.getMaintenanceScheduleById(maintenanceId);
            if (!mainSchedule.getMaintenanceStatus().equals("Ngừng bảo trì")) {
                MaintenanceAssignment mainAssign = this.mainAssignService.getAssignmentByTechnicianIdAndMaintenanceId(existingUser.getId(), maintenanceId);

                if (mainAssign.getIsCap()) {
                    String expenseLast = params.get("expenseLast");
                    String description = params.get("description");

                    if (expenseLast == null) {
                        return ResponseEntity.badRequest().body("Thiếu expenseLast");
                    }

                    Date maintenanceDate = new Date();
                    BigDecimal expense = new BigDecimal(expenseLast);

                    MaintenanceSchedule mainSaved = this.mainScheduleService.getMaintenanceScheduleById(maintenanceId);
                    Device d = this.devService.getDeviceById(mainSaved.getDeviceId().getId());
                    d.setDeviceStatus("Hoạt động");
                    this.devService.addOrUpdateDevice(d);

                    mainSaved.setDescription(description);
                    mainSaved.setExpenseLast(expense);
                    mainSaved.setMaintenanceDate(maintenanceDate);
                    mainSaved.setMaintenanceStatus("Đã bảo trì");
                    this.mainScheduleService.addOrUpdateMaintenanceSchedule(mainSaved);

                    return ResponseEntity.ok().body("Cập nhật thành công");
                } else {
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Bạn không có quyền cập nhật thông tin bảo trì");
                }
            } else {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Bảo trì này đã ngưng hoạt động");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

}
