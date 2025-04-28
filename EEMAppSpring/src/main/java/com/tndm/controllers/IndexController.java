/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.services.DeviceService;
import com.tndm.services.DeviceStatusService;
import com.tndm.services.DeviceTypeService;
import com.tndm.services.FacilityService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
@ControllerAdvice
public class IndexController {
    
    @Autowired
    private DeviceService devService;
    
    @Autowired
    private FacilityService facService;
    
    @Autowired
    private DeviceTypeService deviceTypeService;
    
    @Autowired
    private DeviceStatusService devStatusService;
    
    @ModelAttribute
    public void commonResponse(Model model){
        model.addAttribute("facilities", this.facService.getFacilities());
        model.addAttribute("deviceTypes", this.deviceTypeService.getDeviceTypes());
        model.addAttribute("deviceStatus", this.devStatusService.getDeviceStatus());
    }
    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("devices", this.devService.getDevices(params));
        return "index";
    }

    @RequestMapping("/indexFacilities")
    public String indexFacilities(Model model) {
        model.addAttribute("facilities", this.facService.getFacilities());
        return "indexFacilities";
    }
}
