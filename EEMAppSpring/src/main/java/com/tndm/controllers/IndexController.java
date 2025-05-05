/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.User;
import com.tndm.repositories.impl.DeviceRepositoryImpl;
import com.tndm.services.DeviceService;
import com.tndm.services.DeviceStatusService;
import com.tndm.services.DeviceTypeService;
import com.tndm.services.FacilityService;
import com.tndm.services.FatalLevelService;
import com.tndm.services.ProblemService;
import com.tndm.services.ProblemStatusService;
import com.tndm.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
@ControllerAdvice
public class IndexController {

    @Autowired
    private DeviceService devService;

    @Autowired
    private FacilityService facService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private DeviceStatusService devStatusService;

    @Autowired
    private ProblemStatusService proStatusService;

    @Autowired
    private FatalLevelService fatLevelService;

    @Autowired
    private ProblemService proService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void commonResponse(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("facilities", this.facService.getFacilities());
        model.addAttribute("deviceTypes", this.deviceTypeService.getDeviceTypes());
        model.addAttribute("deviceStatus", this.devStatusService.getDeviceStatus());
        model.addAttribute("problemStatus", this.proStatusService.getProblemStatus());
        model.addAttribute("FatalLevels", this.fatLevelService.getFatalLevel());

        if (userDetails != null) {
            User user = userService.getUserByUsername(userDetails.getUsername());
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
        }
    }

    @RequestMapping("/")
    public String indexDevices(Model model, @RequestParam Map<String, String> params) {
        List<Device> devices = this.devService.getDevices(params);
        model.addAttribute("devices", devices);
        model.addAttribute("countDevices", this.devService.countDevices(params));

        long totalDevices = this.devService.countDevices(params);
        int totalPages = (int) Math.ceil((double) totalDevices / DeviceRepositoryImpl.PAGE_SIZE);
        totalPages = Math.max(1, totalPages);
        model.addAttribute("totalPages", totalPages);

        int currentPage;
        try {
            currentPage = Integer.parseInt(params.getOrDefault("page", "1"));
            if (currentPage < 1) {
                currentPage = 1;
            }
            if (currentPage > totalPages && totalPages > 0) {
                currentPage = totalPages;
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("params", params);

        // Lấy các tham số tìm kiếm và phân trang
        String searchType = params.get("searchType");
        String searchValue = params.get("value");
        String pageParam = params.get("page");

        if (searchType != null && searchValue != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
        }

        return "index-devices";
    }

    @RequestMapping("/index-facilities")
    public String indexFacilities(Model model) {
        return "index-facilities";
    }

    @RequestMapping("/index-users")
    public String indexUser(Model model) {
        model.addAttribute("users", this.userService.getAllUser());
        return "index-users";
    }

    @RequestMapping("/access-deny")
    public String accessDenied() {
        return "access-deny";
    }

    @RequestMapping("/index-problems")
    public String indexProblem(Model model) {
        model.addAttribute("problems", this.proService.getProblem());

        return "index-problems";
    }
}
