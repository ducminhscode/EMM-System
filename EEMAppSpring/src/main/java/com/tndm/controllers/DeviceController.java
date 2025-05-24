/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.pojo.Technician;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.MaintenanceAssignmentService;
import com.tndm.services.MaintenanceScheduleService;
import com.tndm.services.TechnicianService;
import com.tndm.services.UserService;
import java.security.Principal;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private TechnicianService techSer;

    @Autowired
    private MaintenanceScheduleService mainScheduleService;

    @Autowired
    private MaintenanceAssignmentService mainAssignmentService;

    @GetMapping("/devices-by-facilityId")
    @ResponseBody
    public List<Device> getDevicesByFacility(@RequestParam("facilityId") int facilityId) {
        return devSer.getDevicesByFacilityId(facilityId);
    }

    @GetMapping("/devices")
    public String viewDevice(Model model) {
        model.addAttribute("device", new Device());
        return "devices";
    }

    @PostMapping("/devices/add")
    public String createDevice(@ModelAttribute(value = "device") Device d, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        d.setUserId(currentUser);
        if (d.getDeviceStatus() == null) {
            d.setDeviceStatus("Hoạt động");
        }

        Device savedDevice = this.devSer.addOrUpdateDevice(d);
        redirectAttributes.addAttribute("deviceId", savedDevice.getId());

        return "redirect:/devices/maintenance-form/{deviceId}";
    }

    @PostMapping("/devices/edit")
    public String updateDevice(@ModelAttribute(value = "device") Device d, Principal principal) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        d.setUserId(currentUser);
        Device devCheck = this.devSer.getDeviceById(d.getId());
        if (!devCheck.getFacilityId().equals(d.getFacilityId())) {
            MaintenanceSchedule mainSaved = this.mainScheduleService.findTheLastestScheduleByDeviceId(d.getId());
            if (mainSaved.getMaintenanceStatus().equals("Chưa bảo trì")) {
                mainSaved.setMaintenanceStatus("Ngừng bảo trì");
            }
            this.mainScheduleService.addOrUpdateMaintenanceSchedule(mainSaved);
        }
        this.devSer.addOrUpdateDevice(d);

        return "redirect:/";
    }

    @GetMapping("/devices/{deviceId}")
    public String viewDeviceDetail(Model model, @PathVariable(value = "deviceId") int id) {
        model.addAttribute("device", this.devSer.getDeviceById(id));
        return "devices";
    }

    @DeleteMapping("/devices/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "deviceId") int id) {
        this.devSer.deleteDevice(id);
    }

    @GetMapping("/devices/maintenance-form/{deviceId}")
    public String maintenanceScheduleDevice(@PathVariable("deviceId") int id, Model model) {
        model.addAttribute("maintenance", new MaintenanceSchedule());
        Device dv = this.devSer.getDeviceById(id);
        model.addAttribute("technicians", this.techSer.getTechnicianByFacilityId(dv.getFacilityId().getId()));
        model.addAttribute("startDate", dv.getCreatedDate());
        model.addAttribute("deviceId", id);
        return "maintenance-form";
    }

    @PostMapping("/devices/maintenance-form/{deviceId}/add")
    public String createMaintenanceScheduleDevice(@ModelAttribute(value = "maintenance") MaintenanceSchedule m,
            @PathVariable("deviceId") int id,
            @RequestParam("technicianIds") List<Integer> technicianIds,
            @RequestParam("leaderId") int leaderId,
            Principal principal) {

        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        m.setUserId(currentUser);

        Device deviceSaved = devSer.getDeviceById(id);
        m.setDeviceId(deviceSaved);

        m.setMaintenanceStatus("Chưa bảo trì");

        MaintenanceSchedule ms = this.mainScheduleService.addOrUpdateMaintenanceSchedule(m);

        if (technicianIds != null && !technicianIds.isEmpty()) {
            for (Integer techId : technicianIds) {
                MaintenanceAssignment assignment = new MaintenanceAssignment();
                assignment.setMaintenanceScheduleId(ms);
                if (leaderId == techId) {
                    assignment.setIsCap(Boolean.TRUE);
                } else {
                    assignment.setIsCap(Boolean.FALSE);
                }
                Technician technician = techSer.getTechnicianById(techId);
                assignment.setTechnicianId(technician);

                mainAssignmentService.addMaintenanceAssignment(assignment);
            }
        }

        return "redirect:/";
    }
}
