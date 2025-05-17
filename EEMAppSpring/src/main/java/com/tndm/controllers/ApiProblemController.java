/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Problem;
import com.tndm.pojo.ProblemStatus;
import com.tndm.pojo.RepairHistory;
import com.tndm.pojo.RepairType;
import com.tndm.pojo.User;
import com.tndm.services.ProblemService;
import com.tndm.services.ProblemStatusService;
import com.tndm.services.RepairHistoryService;
import com.tndm.services.RepairTypeService;
import com.tndm.services.TechnicianService;
import com.tndm.services.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class ApiProblemController {

    @Autowired
    private ProblemService proService;

    @Autowired
    private RepairHistoryService repHistoryService;

    @Autowired
    private TechnicianService techService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private RepairTypeService repTypeService;

    @Autowired
    private ProblemStatusService proStatusService;

    @GetMapping("/problem/technician/{technicianId}")
    @CrossOrigin
    public ResponseEntity<?> getProblemByTechnicianId(@PathVariable("technicianId") int id) {
        List<Problem> problems = this.proService.getProblemsByTechnicianId(id);
        List<Map<String, Object>> listData = new ArrayList<>();

        for (Problem p : problems) {
            Map<String, Object> data = new HashMap<>();
            data.put("description", p.getDescription());
            data.put("happenedDate", p.getHappenedDate());
            data.put("fatalLevel", p.getFatalLevelId().getName());
            data.put("deviceName", p.getDeviceId().getName());

            listData.add(data);
        }

        return ResponseEntity.ok(listData);
    }

    @PatchMapping("/problem/technician/{problemId}")
    @CrossOrigin
    public ResponseEntity<?> updateRepairHistory(
            Principal principal,
            @RequestParam Map<String, String> params,
            @PathVariable("problemId") int problemId) {
        try {
            String username = principal.getName();

            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }

            String endDateStr = params.get("endDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int repairTypeId = Integer.parseInt(params.get("repairTypeId"));

            RepairType repTypeSaved = this.repTypeService.getRepairTypeById(repairTypeId);
            RepairHistory repSaved = this.repHistoryService.getRepairHistoryByProblemIdAndTechnicianId(problemId, existingUser.getId());
            Problem p = this.proService.getProblemById(problemId);
            ProblemStatus pStatus = this.proStatusService.getProblemStatusByName("Đã sửa chữa");

            repSaved.setDescription(params.get("description"));
            repSaved.setExpense(new BigDecimal(params.get("expense")));
            repSaved.setEndDate(sdf.parse(endDateStr));
            repSaved.setTypeId(repTypeSaved);
            this.repHistoryService.addOrUpdateRepairHistory(repSaved);

            List<RepairHistory> repListSaved = this.repHistoryService.getRepairHistoriesByProblemId(problemId);

            boolean flag = repListSaved.stream().allMatch(RepairHistory::isDone);

            if (flag) {
                p.setStatusId(pStatus);
                this.proService.addOrUpdateProblem(p);
            }

            return new ResponseEntity<>(params, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

}
