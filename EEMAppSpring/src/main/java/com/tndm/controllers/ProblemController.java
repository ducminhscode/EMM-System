/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.Problem;
import com.tndm.pojo.RepairHistory;
import com.tndm.pojo.Technician;
import com.tndm.services.DeviceService;
import com.tndm.services.ProblemService;
import com.tndm.services.RepairHistoryService;
import com.tndm.services.TechnicianService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ADMIN
 */
@Controller
public class ProblemController {

    @Autowired
    private RepairHistoryService repHistoryService;

    @Autowired
    private ProblemService proService;

    @Autowired
    private DeviceService devService;
    
    @Autowired
    private TechnicianService techService;

    @GetMapping("/problems/{problemId}")
    public String getProblemDetails(@PathVariable("problemId") int id, Model model) {
        model.addAttribute("problem", proService.getProblemById(id));
        model.addAttribute("repairs", repHistoryService.getRepairHistoriesByProblemId(id));

        return "problems";
    }

    @PostMapping("/problem-add-technician/{problemId}")
    public String updateProblemAndAddTech(@PathVariable("problemId") int id,
            @RequestParam("technicianIds") List<Integer> technicianIds) {

        Problem problem = this.proService.getProblemById(id);
        problem.setProblemStatus("Xác nhận");
        Device deviceSaved = this.devService.getDeviceById(problem.getDeviceId().getId());
        deviceSaved.setDeviceStatus("Sửa chữa");
        this.devService.addOrUpdateDevice(deviceSaved);
        Problem problemSaved = this.proService.addOrUpdateProblem(problem);

        if (technicianIds != null && !technicianIds.isEmpty()) {
            for (Integer techId : technicianIds) {
                RepairHistory repairSave = new RepairHistory();
                repairSave.setProblemId(problemSaved);
                repairSave.setStartDate(new Date());

                Technician technician = techService.getTechnicianById(techId);
                repairSave.setTechnicianId(technician);

                this.repHistoryService.addOrUpdateRepairHistory(repairSave);
            }
        }

        return "redirect:/index-problems";
    }

    @DeleteMapping("/problems/{problemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "problemId") int id) {
        this.proService.deleteProblem(id);
    }
}
