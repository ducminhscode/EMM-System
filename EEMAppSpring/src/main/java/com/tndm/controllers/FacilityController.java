/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Facility;
import com.tndm.services.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class FacilityController {
    @Autowired
    private FacilityService facSer;
    
    @GetMapping("/facilities")
    public String facilityView(Model model) {
        model.addAttribute("facility", new Facility());
        return "facilities";
    }

    @PostMapping("/facilities/add")
    public String createDevice(@ModelAttribute(value = "facility") Facility d) {
        this.facSer.addOrUpdateFacility(d);
        return "redirect:/indexFacilities";
    }
    
    @GetMapping("/facilities/{facilityId}")
    public String facilityView(Model model, @PathVariable(value = "facilityId") int id) {
        model.addAttribute("facility", this.facSer.getFacilityById(id));
        return "facilities";
    }
}
