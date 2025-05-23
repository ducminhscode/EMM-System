/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.services.DeviceService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiDeviceController {

    @Autowired
    private DeviceService devService;

    @GetMapping("/devices")
    @ResponseBody
    public ResponseEntity<?> getDevice(@RequestParam Map<String, String> params) {
        List<Device> devices = devService.getDevices(params);
        List<Map<String, Object>> listData = new ArrayList<>();

        for (Device d : devices) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", d.getId());
            data.put("name", d.getName());
            data.put("manufacturer", d.getManufacturer());
            data.put("statusId", d.getDeviceStatus());
            data.put("typeId", d.getTypeId().getName());
            data.put("facilityId", d.getFacilityId().getName());
            data.put("purchaseDate", d.getPurchaseDate());
            listData.add(data);
        }
        return ResponseEntity.ok(listData);
    }
}
