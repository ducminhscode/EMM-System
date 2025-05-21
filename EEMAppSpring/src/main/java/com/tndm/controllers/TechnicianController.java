/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Technician;
import com.tndm.services.TechnicianService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ADMIN
 */
@Controller
public class TechnicianController {

    @Autowired
    private TechnicianService techService;

    @GetMapping("/technicians-by-facilityId")
    @ResponseBody
    public List<Technician> getTechniciansByFacility(@RequestParam("facilityId") int facilityId) {
        return techService.getTechnicianByFacilityId(facilityId);
    }
}
