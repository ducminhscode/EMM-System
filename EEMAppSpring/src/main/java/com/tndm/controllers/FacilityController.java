/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Facility;
import com.tndm.pojo.User;
import com.tndm.services.DeviceService;
import com.tndm.services.FacilityService;
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
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class FacilityController {

    @Autowired
    private FacilityService facSer;

    @Autowired
    private UserService usrSer;

    @GetMapping("/facilities")
    public String viewFacility(Model model) {
        model.addAttribute("facility", new Facility());
        return "facilities";
    }

    @PostMapping("/facilities/add")
    public String createFacility(@ModelAttribute(value = "facility") Facility f, Principal principal) {
        String username = principal.getName();
        User currentUser = usrSer.getUserByUsername(username);
        f.setUserId(currentUser);

        this.facSer.addOrUpdateFacility(f);
        return "redirect:/index-facilities";
    }

    @GetMapping("/facilities/{facilityId}")
    public String viewFacilityDetail(Model model, @PathVariable(value = "facilityId") int id) {
        model.addAttribute("facility", this.facSer.getFacilityById(id));
        return "facilities";
    }

    @DeleteMapping("/facilities/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "facilityId") int id) {
        this.facSer.deleteFacility(id);
    }
}
