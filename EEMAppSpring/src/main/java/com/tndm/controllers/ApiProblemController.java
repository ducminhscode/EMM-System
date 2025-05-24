/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.FatalLevel;
import com.tndm.pojo.Problem;
import com.tndm.pojo.RepairHistory;
import com.tndm.pojo.RepairType;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.FatalLevelService;
import com.tndm.services.ProblemService;
import com.tndm.services.RepairHistoryService;
import com.tndm.services.RepairTypeService;
import com.tndm.services.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiProblemController {

    @Autowired
    private ProblemService proService;

    @Autowired
    private RepairHistoryService repHistoryService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private FatalLevelService fatalLevelService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private RepairTypeService repTypeService;

    @GetMapping("/problem/technician/{technicianId}")
    @CrossOrigin
    public ResponseEntity<?> getProblemByTechnicianId(@PathVariable("technicianId") int id, @RequestParam("page") String page) {

        List<Problem> problems = this.proService.getProblemsByTechnicianId(id, page);
        List<Map<String, Object>> listData = new ArrayList<>();

        for (Problem p : problems) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", p.getId());
            data.put("description", p.getDescription());
            data.put("happenedDate", p.getHappenedDate());
            data.put("problemStatus", p.getProblemStatus());
            data.put("fatalLevel", p.getFatalLevelId().getName());
            data.put("deviceName", p.getDeviceId().getName());
            data.put("isDone", this.repHistoryService.getRepairHistoryByProblemIdAndTechnicianId(p.getId(), id).isDone());

            listData.add(data);
        }

        return ResponseEntity.ok(listData);
    }

    @PatchMapping("secure/problem/{problemId}")
    @CrossOrigin
    public ResponseEntity<?> updateRepairHistory(
            @PathVariable("problemId") int problemId,
            Principal principal) {

        try {
            String username = principal.getName();
            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }

            Problem p = this.proService.getProblemById(problemId);
            RepairHistory repSaved = this.repHistoryService.getRepairHistoryByProblemIdAndTechnicianId(problemId, existingUser.getId());
            if (repSaved == null) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Bạn không có trong danh sách kĩ sư sửa chữa");
            } else {
                p.setProblemStatus("Đang sửa chữa");
                this.proService.addOrUpdateProblem(p);
            }

            return ResponseEntity.ok().body("Cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @PatchMapping("secure/problem/technician/{problemId}")
    @CrossOrigin
    public ResponseEntity<?> updateRepairHistory(
            @PathVariable("problemId") int problemId,
            Principal principal,
            @RequestParam Map<String, String> params) {

        try {
            String username = principal.getName();
            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }

            String expenseStr = params.get("expense");
            String description = params.get("description");
            int repairTypeId = Integer.parseInt(params.get("repairTypeId"));

            if (expenseStr == null) {
                return ResponseEntity.badRequest().body("Thiếu expense");
            }

            Date endDate = new Date();
            BigDecimal expense = new BigDecimal(expenseStr);

            RepairType repTypeSaved = this.repTypeService.getRepairTypeById(repairTypeId);
            RepairHistory repSaved = this.repHistoryService.getRepairHistoryByProblemIdAndTechnicianId(problemId, existingUser.getId());
            Problem p = this.proService.getProblemById(problemId);

            repSaved.setDescription(description);
            repSaved.setExpense(expense);
            repSaved.setEndDate(endDate);
            repSaved.setTypeId(repTypeSaved);
            this.repHistoryService.addOrUpdateRepairHistory(repSaved);

            List<RepairHistory> repListSaved = this.repHistoryService.getRepairHistoriesByProblemId(problemId);
            boolean flag = repListSaved.stream().allMatch(RepairHistory::isDone);

            if (flag) {
                p.setProblemStatus("Đã sửa chữa");
                Device d = this.deviceService.getDeviceById(p.getDeviceId().getId());
                d.setDeviceStatus("Hoạt động");
                this.deviceService.addOrUpdateDevice(d);
                this.proService.addOrUpdateProblem(p);
            }

            return ResponseEntity.ok().body("Cập nhật thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @PostMapping("secure/problem")
    @CrossOrigin
    public ResponseEntity<?> createProblem(
            @RequestParam Map<String, String> params,
            Principal principal) {

        try {
            String username = principal.getName();
            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }

            String deviceIdStr = params.get("deviceId");
            String description = params.get("description");
            String fatalLevelIdStr = params.get("fatalLevelId");
            String happenedDateStr = params.get("happenedDate");

            if (deviceIdStr == null || fatalLevelIdStr == null || happenedDateStr == null) {
                return ResponseEntity.badRequest().body("Thiếu tham số bắt buộc");
            }

            int deviceId;
            int fatalLevelId;
            try {
                deviceId = Integer.parseInt(deviceIdStr);
                fatalLevelId = Integer.parseInt(fatalLevelIdStr);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("deviceId hoặc fatalLevelId phải là số nguyên");
            }

            Date happenedDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                happenedDate = dateFormat.parse(happenedDateStr);
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body("Định dạng happenedDate không hợp lệ");
            }

            Device device = this.deviceService.getDeviceById(deviceId);
            if (device == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thiết bị");
            }

            if (!device.getDeviceStatus().equals("Hoạt động")) {
                return ResponseEntity.badRequest().body("Thiết bị không hoạt động");
            }

            FatalLevel fatalLevel = this.fatalLevelService.getFatalLevelById(fatalLevelId);
            if (fatalLevel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy mức độ nghiêm trọng");
            }

            Problem problem = new Problem();
            problem.setDeviceId(device);
            problem.setDescription(description);
            problem.setFatalLevelId(fatalLevel);
            problem.setHappenedDate(happenedDate);
            problem.setProblemStatus("Chưa xác nhận");
            problem.setUserId(existingUser);
            problem.setCreatedDate(new Date());
            problem.setUpdatedDate(new Date());

            this.proService.addOrUpdateProblem(problem);

            return ResponseEntity.ok().body("Tạo sự cố thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo sự cố: " + e.getMessage());
        }
    }

}
