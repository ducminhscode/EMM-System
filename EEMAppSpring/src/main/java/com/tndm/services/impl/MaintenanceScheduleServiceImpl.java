/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Device;
import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.repositories.MaintenanceAssignmentRepository;
import com.tndm.repositories.MaintenanceScheduleRepository;
import com.tndm.services.DeviceService;
import com.tndm.services.MailService;
import com.tndm.services.MaintenanceScheduleService;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class MaintenanceScheduleServiceImpl implements MaintenanceScheduleService {

    @Autowired
    private MaintenanceScheduleRepository mainScheduleRepo;

    @Autowired
    private MaintenanceAssignmentRepository mainAssignRepo;

    @Autowired
    private MailService mailSer;

    @Autowired
    private DeviceService devService;

    @Override
    public List<MaintenanceSchedule> getMaintenanceSchedule(Map<String, String> params) {
        return this.mainScheduleRepo.getMaintenanceSchedule(params);
    }

    @Override
    public MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m) {
        return this.mainScheduleRepo.addOrUpdateMaintenanceSchedule(m);
    }

    @Override
    public void deleteMaintenanceSchedule(int id) {
        this.mainScheduleRepo.deleteMaintenanceSchedule(id);
    }

    @Override
    public MaintenanceSchedule getMaintenanceScheduleById(int id) {
        return this.mainScheduleRepo.getMaintenanceScheduleById(id);
    }

    @Override
    @Scheduled(cron = "0 */3 * * * *")
    public void notifySchedule() {
        List<MaintenanceSchedule> listMaintenances = this.mainScheduleRepo.findSchedulesToNotify();
        for (MaintenanceSchedule maintenance : listMaintenances) {
            List<MaintenanceAssignment> listAssigns = this.mainAssignRepo.getAssignmentByMaintenanceId(maintenance.getId());
            for (MaintenanceAssignment mainAssign : listAssigns) {
                mailSer.sendMail(mainAssign.getTechnicianId().getUser().getEmail(), "Tới hạn bảo trì", maintenance.getTitle());
            }
        }
    }

    @Override
    @Scheduled(cron = "0 */3 * * * *")
    public void createNewSchedule() {
        List<Device> devList = this.devService.getAllDevices();

        for (Device d : devList) {
            MaintenanceSchedule maintenance = this.mainScheduleRepo.findTheLastestScheduleByDeviceId(d.getId()); 
            if (maintenance.getFrequency() != null || maintenance.getEndDate() != null || "Đã bảo trì".equals(maintenance.getMaintenanceStatus())) {
                Calendar calStart = Calendar.getInstance();
                Calendar calEnd = Calendar.getInstance();
                calStart.setTime(maintenance.getStartDate());
                calEnd.setTime(maintenance.getEndDate());

                switch (maintenance.getFrequency()) {
                    case "Hàng ngày":
                        calStart.add(Calendar.DAY_OF_MONTH, 1);
                        calEnd.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case "Hàng tuần":
                        calStart.add(Calendar.WEEK_OF_YEAR, 1);
                        calEnd.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case "Hàng tháng":
                        calStart.add(Calendar.MONTH, 1);
                        calEnd.add(Calendar.MONTH, 1);
                        break;
                    case "Hàng quý":
                        calStart.add(Calendar.MONTH, 3);
                        calEnd.add(Calendar.MONTH, 3);
                        break;
                    case "Hàng năm":
                        calStart.add(Calendar.YEAR, 1);
                        calEnd.add(Calendar.YEAR, 1);
                        break;
                    default:
                        continue;
                }

                MaintenanceSchedule newSchedule = new MaintenanceSchedule();
                newSchedule.setTypeId(maintenance.getTypeId());
                newSchedule.setUserId(maintenance.getUserId());
                newSchedule.setDeviceId(maintenance.getDeviceId());
                newSchedule.setStartDate(calStart.getTime());
                newSchedule.setEndDate(calEnd.getTime());
                newSchedule.setTitle(maintenance.getTitle());
                newSchedule.setDescription(maintenance.getDescription());
                newSchedule.setExpenseFirst(maintenance.getExpenseFirst());
                newSchedule.setFrequency(maintenance.getFrequency());
                newSchedule.setMaintenanceStatus("Chưa bảo trì");

                MaintenanceSchedule mainSaved = this.mainScheduleRepo.addOrUpdateMaintenanceSchedule(newSchedule);

                List<MaintenanceAssignment> mainAssign = this.mainAssignRepo.getAssignmentByMaintenanceId(maintenance.getId());

                if (mainAssign != null) {
                    for (MaintenanceAssignment m : mainAssign) {
                        MaintenanceAssignment newAssign = new MaintenanceAssignment();
                        newAssign.setTechnicianId(m.getTechnicianId());
                        newAssign.setMaintenanceScheduleId(mainSaved);
                        this.mainAssignRepo.addMaintenanceAssignment(newAssign);
                    }
                }
            }
        }
    }

    @Override
    public long countMaintenances(Map<String, String> params) {
        return this.mainScheduleRepo.countMaintenances(params);
    }

    @Override
    @Scheduled(cron = "0 */3 * * * *")
    public void changeMaintenanceStatus() {
        List<MaintenanceSchedule> listMaintenances = this.mainScheduleRepo.findScheduleOverTheTime();
        for (MaintenanceSchedule main : listMaintenances) {
            main.setMaintenanceStatus("Quá hạn bảo trì");
            mainScheduleRepo.addOrUpdateMaintenanceSchedule(main);
        }
    }

    @Override
    public List<MaintenanceSchedule> findSchedulesToNotifyToTechnician(int technicianId, String pageStr) {
        return this.mainScheduleRepo.findSchedulesToNotifyToTechnician(technicianId, pageStr);
    }

    @Override
    public List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndTime(int deviceId, int month, int year) {
        return this.mainScheduleRepo.getMaintenanceScheduleByDeviceIdAndTime(deviceId, month, year);
    }

    @Override
    public List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndYear(int deviceId, int year) {
        return this.mainScheduleRepo.getMaintenanceScheduleByDeviceIdAndYear(deviceId, year);
    }
}
