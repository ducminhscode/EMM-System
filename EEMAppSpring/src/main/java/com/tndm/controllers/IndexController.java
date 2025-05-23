/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.Device;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.pojo.Problem;
import com.tndm.pojo.RepairHistory;
import com.tndm.pojo.User;
import com.tndm.repositories.impl.DeviceRepositoryImpl;
import com.tndm.repositories.impl.FacilityRepositoryImpl;
import com.tndm.repositories.impl.MaintenanceScheduleRepositoryImpl;
import com.tndm.repositories.impl.ProblemRepositoryImpl;
import com.tndm.repositories.impl.UserRepositoryImpl;
import com.tndm.services.DeviceService;
import com.tndm.services.DeviceTypeService;
import com.tndm.services.FacilityService;
import com.tndm.services.MaintenanceScheduleService;
import com.tndm.services.MaintenanceTypeService;
import com.tndm.services.ProblemService;
import com.tndm.services.RepairHistoryService;
import com.tndm.services.UserService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private ProblemService proService;

    @Autowired
    private UserService userService;

    @Autowired
    private MaintenanceTypeService mainTypeService;

    @Autowired
    private MaintenanceScheduleService mainScheduleService;

    @Autowired
    private RepairHistoryService repHistoryService;

    @ModelAttribute
    public void commonResponse(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam Map<String, String> params) {
        model.addAttribute("facilities", this.facService.getFacilities(null, false));
        model.addAttribute("deviceTypes", this.deviceTypeService.getDeviceTypes());
        model.addAttribute("maintenanceTypes", this.mainTypeService.getMaintenanceTypes());
        model.addAttribute("countMaintenances", this.mainScheduleService.countMaintenances(params));
        model.addAttribute("countProblems", this.proService.countProblems(params));

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
        model.addAttribute("countDevicesActive", this.devService.countDevicesActive(params));

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

        String searchType = params.get("searchType");
        String searchValue = params.get("value");

        if (searchType != null && searchValue != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
        }

        return "index-devices";
    }

    @RequestMapping("/index-facilities")
    public String indexFacilities(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("facilities", this.facService.getFacilities(params, true));
        model.addAttribute("countFacilities", this.facService.countFacilities(params));

        long totalFacilities = this.facService.countFacilities(params);
        int totalPages = (int) Math.ceil((double) totalFacilities / FacilityRepositoryImpl.PAGE_SIZE);
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

        String searchType = params.get("searchType");
        String searchValue = params.get("value");

        if (searchType != null && searchValue != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
        }

        return "index-facilities";
    }

    @RequestMapping("/index-users")
    public String indexUser(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("countActiveUsers", this.userService.countActiveUsers());

        List<User> users = this.userService.getUsers(params);
        model.addAttribute("users", users);
        model.addAttribute("countUsers", this.userService.countUsers(params));

        long totalUsers = this.userService.countUsers(params);
        int totalPages = (int) Math.ceil((double) totalUsers / UserRepositoryImpl.PAGE_SIZE);
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

        String searchType = params.get("searchType");
        String searchValue = params.get("value");

        if (searchType != null && searchValue != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
        }

        return "index-users";
    }

    @RequestMapping("/access-deny")
    public String accessDenied() {
        return "access-deny";
    }

    @RequestMapping("/index-problems")
    public String indexProblem(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("problems", this.proService.getProblem(params));

        long totalProblems = this.proService.countProblems(params);
        int totalPages = (int) Math.ceil((double) totalProblems / ProblemRepositoryImpl.PAGE_SIZE);
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

        return "index-problems";
    }

    @RequestMapping("/index-maintenances")
    public String indexMaintenance(Model model, @RequestParam Map<String, String> params) {

        List<MaintenanceSchedule> mainSche = this.mainScheduleService.getMaintenanceSchedule(params);
        model.addAttribute("maintenances", mainSche);
        model.addAttribute("countMaintenances", this.mainScheduleService.countMaintenances(params));

        long totalMainSche = this.mainScheduleService.countMaintenances(params);
        int totalPages = (int) Math.ceil((double) totalMainSche / MaintenanceScheduleRepositoryImpl.PAGE_SIZE);
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

        String searchType = params.get("searchType");
        String searchValue = params.get("value");

        if (searchType != null && searchValue != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchValue", searchValue);
        }

        return "index-maintenances";
    }

    @RequestMapping("/index-reports")
    public String getReports(Model model,
            @RequestParam(name = "deviceId", required = false) Integer deviceId,
            @RequestParam(name = "typeId", required = false) Integer typeId) {

        List<Problem> problemsToDisplay = new ArrayList<>();
        List<Device> allDevices = devService.getAllDevices();
        List<Integer> deviceIds = new ArrayList<>();

        if (deviceId != null) {
            deviceIds.add(deviceId);
            problemsToDisplay = proService.getProblemsByDeviceIds(deviceIds);
        } else if (typeId != null) {
            List<Device> devicesByType = this.devService.getDevicesByTypeId(typeId);
            deviceIds = devicesByType.stream().map(Device::getId).collect(Collectors.toList());
            problemsToDisplay = proService.getProblemsByDeviceIds(deviceIds);
        } else {
            deviceIds = allDevices.stream().map(Device::getId).collect(Collectors.toList());
            problemsToDisplay = proService.getProblemsByDeviceIds(deviceIds);
        }

        Map<Integer, List<RepairHistory>> repairMap = problemsToDisplay.stream()
                .collect(Collectors.toMap(Problem::getId, problem -> repHistoryService.getRepairHistoriesByProblemId(problem.getId())));

        Map<Integer, BigDecimal> repairTotal = problemsToDisplay.stream()
                .collect(Collectors.toMap(
                        Problem::getId,
                        problem -> {
                            List<RepairHistory> repairs = repHistoryService.getRepairHistoriesByProblemId(problem.getId());
                            return repairs.stream()
                                    .filter(r -> r.getExpense() != null)
                                    .map(RepairHistory::getExpense)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                        }
                ));

        model.addAttribute("repairTotal", repairTotal);
        model.addAttribute("repairDetail", repairMap);
        model.addAttribute("devices", allDevices);
        model.addAttribute("problems", problemsToDisplay);
        model.addAttribute("selectedDeviceId", deviceId);
        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("typeId", typeId);

        return "index-reports";
    }
}
