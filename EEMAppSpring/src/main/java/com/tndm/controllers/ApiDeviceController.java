/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.services.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/devices-by-facilityId")
    @ResponseBody
    public List<Device> getDevicesByFacility(@RequestParam("facilityId") int facilityId) {
        return devService.getDevicesByFacilityId(facilityId);
    }

    @GetMapping("/devices")
    @ResponseBody
    public List<Device> getDevice(@RequestParam Map<String, String> params) {
        return devService.getDevices(params);
    }

}
