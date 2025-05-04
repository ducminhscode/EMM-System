/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.services.ProblemService;
import com.tndm.services.RepairHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/problems/{problemId}")
    public String getProblemDetails(@PathVariable("problemId") int id,Model model) {
        // Thêm cả 2 vào model
        model.addAttribute("problem", proService.getProblemById(id));
        model.addAttribute("repairs", repHistoryService.getRepairHistoriesByProblemId(id));

        return "problems";
    }
}
