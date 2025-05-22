package com.tndm.controllers;


import com.tndm.pojo.FatalLevel;
import com.tndm.services.FatalLevelService;
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Tran Nguyen Duc Minh
 */
@RestController
@RequestMapping("/api")
public class ApiFatalLevelController {

    @Autowired
    private FatalLevelService fatalLevelService;

    @GetMapping("/fatal-level")
    @CrossOrigin
    public ResponseEntity<?> getFatalLevel() {
        try {
            List<FatalLevel> fatalLvl = this.fatalLevelService.getFatalLevel();
            List<Map<String, Object>> listData = new ArrayList<>();

            for (FatalLevel fl : fatalLvl) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", fl.getId());
                data.put("name", fl.getName());
                listData.add(data);
            }

            return ResponseEntity.ok(listData);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy loại sửa chữa: " + e.getMessage());
        }
    }
}
