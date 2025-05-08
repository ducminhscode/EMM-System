/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.pojo.User;
import com.tndm.services.MaintenanceAssignmentService;
import com.tndm.services.MaintenanceScheduleService;
import com.tndm.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ADMIN
 */
@Controller
public class MaintenanceController {

    @Autowired
    private MaintenanceScheduleService mainScheduleService;

    @Autowired
    private MaintenanceAssignmentService mainAssignmentService;
    
    @Autowired
    private UserService usrSer;

    @GetMapping("/maintenances/{maintenanceId}")
    public String getProblemDetails(@PathVariable("maintenanceId") int id, Model model) {
        model.addAttribute("schedules", mainScheduleService.getMaintenanceScheduleById(id));
        model.addAttribute("assignments", mainAssignmentService.getAssignmentByMaintenanceId(id));
        return "maintenances";
    }

    @GetMapping("/maintenance-form/{maintenanceId}")
    public String viewMaintenanceDetail(Model model, @PathVariable(value = "maintenanceId") int id) {
        model.addAttribute("maintenance", this.mainScheduleService.getMaintenanceScheduleById(id));
        return "maintenance-form";
    }

    @PostMapping("/maintenances/add")
    public String createMaintenanceSchedule(@ModelAttribute(value = "maintenance") MaintenanceSchedule m, Principal principal) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        m.setUserId(currentUser);

        this.mainScheduleService.addOrUpdateMaintenanceSchedule(m);
        return "redirect:/index-maintenances";
    }

    @DeleteMapping("/maintenances/{maintenanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "maintenanceId") int id) {
        this.mainScheduleService.deleteMaintenanceSchedule(id);
    }
}
