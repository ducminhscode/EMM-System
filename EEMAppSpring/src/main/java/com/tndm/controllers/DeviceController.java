/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class DeviceController {

    @Autowired
    private DeviceService devSer;
    
    @Autowired
    private UserService usrSer;


    @GetMapping("/devices")
    public String viewDevice(Model model) {
        model.addAttribute("device", new Device());
        return "devices";
    }

    @PostMapping("/devices/add")
    public String createDevice(@ModelAttribute(value = "device") Device d, Principal principal) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        d.setUserId(currentUser);
        
        this.devSer.addOrUpdateDevice(d);
        return "redirect:/";
    }

    @GetMapping("/devices/{deviceId}")
    public String viewDeviceDetail(Model model, @PathVariable(value = "deviceId") int id) {
        model.addAttribute("device", this.devSer.getDeviceById(id));
        return "devices";
    }
    
}
