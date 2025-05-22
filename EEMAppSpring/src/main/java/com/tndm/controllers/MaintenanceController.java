/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.pojo.Problem;
import com.tndm.pojo.RepairHistory;
import com.tndm.pojo.Technician;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.MaintenanceAssignmentService;
import com.tndm.services.MaintenanceScheduleService;
import com.tndm.services.TechnicianService;
import com.tndm.services.UserService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private DeviceService devService;

    @Autowired
    private TechnicianService techService;

    @GetMapping("/maintenance-result/{maintenanceId}")
    public String getProblemDetails(@PathVariable("maintenanceId") int id, Model model) {
        model.addAttribute("schedules", mainScheduleService.getMaintenanceScheduleById(id));
        model.addAttribute("assignments", mainAssignmentService.getAssignmentByMaintenanceId(id));
        return "maintenance-result";
    }

    @GetMapping("/maintenances")
    public String showMaintenanceForm(Model model) {
        model.addAttribute("maintenance", new MaintenanceSchedule());
        return "maintenances";
    }

    @GetMapping("/maintenance-edit/{maintenanaceId}")
    public String maintenanceScheduleDevice(@PathVariable("maintenanaceId") int id, Model model) {
        MaintenanceSchedule mainSave = this.mainScheduleService.getMaintenanceScheduleById(id);
        List<MaintenanceAssignment> mainAssignSave = this.mainAssignmentService.getAssignmentByMaintenanceId(id);
        model.addAttribute("maintenance", mainSave);
        model.addAttribute("technicians", this.techService.getTechnicianByFacilityId(mainSave.getDeviceId().getFacilityId().getId()));
        List<Integer> selectedTechIds = mainAssignSave.stream().map(assign -> assign.getTechnicianId().getId()).collect(Collectors.toList());
        model.addAttribute("selectedTechIds", selectedTechIds);
        int leaderId = 0;
        for (MaintenanceAssignment mainSaved : mainAssignSave) {
            if (mainSaved.getIsCap() == true) {
                leaderId = mainSaved.getTechnicianId().getId();
            }
        }
        model.addAttribute("leaderId", leaderId);
        return "maintenance-edit";
    }

    @PostMapping("/maintenance-edit/add")
    public String updateMaintenanceSchedule(@ModelAttribute(value = "maintenance") MaintenanceSchedule m,
            @RequestParam("technicianIds") List<Integer> technicianIds,
            @RequestParam("leaderId") int leaderId,
            Principal principal) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        m.setUserId(currentUser);

        MaintenanceSchedule ms = this.mainScheduleService.addOrUpdateMaintenanceSchedule(m);
        List<MaintenanceAssignment> mainAssignSaved = this.mainAssignmentService.getAssignmentByMaintenanceId(ms.getId());

        for (MaintenanceAssignment mainAssign : mainAssignSaved) {
            this.mainAssignmentService.deleteMaintenanceAssignment(mainAssign.getId());
        }

        if (technicianIds != null && !technicianIds.isEmpty()) {
            for (Integer techId : technicianIds) {
                MaintenanceAssignment assignment = new MaintenanceAssignment();
                assignment.setMaintenanceScheduleId(ms);
                if (leaderId == techId) {
                    assignment.setIsCap(Boolean.TRUE);
                } else {
                    assignment.setIsCap(Boolean.FALSE);
                }
                Technician technician = techService.getTechnicianById(techId);
                assignment.setTechnicianId(technician);

                mainAssignmentService.addMaintenanceAssignment(assignment);
            }
        }

        return "redirect:/index-maintenances";
    }

    @PostMapping("/maintenances/add")
    public String createMaintenanceSchedule(@ModelAttribute(value = "maintenance") MaintenanceSchedule m,
            @RequestParam("technicianIds") List<Integer> technicianIds,
            @RequestParam("deviceIds") List<Integer> deviceIds,
            @RequestParam("leaderId") int leaderId,
            Principal principal) {

        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);

        if (deviceIds != null && !deviceIds.isEmpty()) {
            for (Integer devId : deviceIds) {
                Device deviceSaved = devService.getDeviceById(devId);

                MaintenanceSchedule mainSaved = new MaintenanceSchedule(m.getId(), m.getStartDate(), m.getEndDate(), m.getTitle(), m.getFrequency(), "Chưa bảo trì");
                mainSaved.setExpenseFirst(m.getExpenseFirst());
                mainSaved.setDeviceId(deviceSaved);
                mainSaved.setUserId(currentUser);
                mainSaved.setTypeId(m.getTypeId());

                MaintenanceSchedule ms = this.mainScheduleService.addOrUpdateMaintenanceSchedule(mainSaved);

                if (technicianIds != null && !technicianIds.isEmpty()) {
                    for (Integer techId : technicianIds) {
                        MaintenanceAssignment assignment = new MaintenanceAssignment();
                        assignment.setMaintenanceScheduleId(ms);
                        if (leaderId == techId) {
                            assignment.setIsCap(Boolean.TRUE);
                        } else {
                            assignment.setIsCap(Boolean.FALSE);
                        }
                        Technician technician = techService.getTechnicianById(techId);
                        assignment.setTechnicianId(technician);

                        mainAssignmentService.addMaintenanceAssignment(assignment);
                    }
                }
            }
        }
        return "redirect:/index-maintenances";
    }

    @DeleteMapping("/maintenances/{maintenanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "maintenanceId") int id) {
        this.mainScheduleService.deleteMaintenanceSchedule(id);
    }

    @GetMapping("/maintenance-chart")
    public String maintenanceChart(
            Model model,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "deviceId", required = false) Integer deviceId,
            @RequestParam(name = "month", required = false) Integer month) {

        List<MaintenanceSchedule> schedules = Collections.EMPTY_LIST;
        if (deviceId != null && year != null && month != null) {
            schedules = this.mainScheduleService.getMaintenanceScheduleByDeviceIdAndTime(deviceId, month, year);
        }
        model.addAttribute("devices", this.devService.getAllDevices());
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("schedules", schedules);

        return "maintenance-chart";
    }

}
