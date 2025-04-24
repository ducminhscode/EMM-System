/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class DeviceController {
    
    @Autowired
    private DeviceService devSer;
    
    @GetMapping("/devices")
    public String deviceView(Model model){
        model.addAttribute("product", new Device());
        return  "devices";
    }
    
    @PostMapping("/add")
    public String createDevice(@ModelAttribute(value = "device") Device p){
        this.devSer.addOrUpdate(p);
        
        return "redirect:/";
    }
}
