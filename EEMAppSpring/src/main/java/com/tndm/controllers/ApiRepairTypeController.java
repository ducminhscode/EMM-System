/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.RepairType;
import com.tndm.services.RepairTypeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiRepairTypeController {

    @Autowired
    private RepairTypeService repTypeService;

    @GetMapping("/repair-type")
    @CrossOrigin
    public ResponseEntity<?> getRepairType() {
        try {
            List<RepairType> repTypes = this.repTypeService.getAllRepairType();
            List<Map<String, Object>> listData = new ArrayList<>();

            for (RepairType repSave : repTypes) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", repSave.getId());
                data.put("name", repSave.getName());
                listData.add(data);
            }
            
            return ResponseEntity.ok(listData);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy loại sửa chữa: " + e.getMessage());
        }
    }
}
