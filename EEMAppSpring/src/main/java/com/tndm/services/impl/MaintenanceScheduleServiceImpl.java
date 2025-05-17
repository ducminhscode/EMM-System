/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.repositories.MaintenanceAssignmentRepository;
import com.tndm.repositories.MaintenanceScheduleRepository;
import com.tndm.services.MailService;
import com.tndm.services.MaintenanceScheduleService;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<MaintenanceSchedule> getMaintenanceSchedule() {
        return this.mainScheduleRepo.getMaintenanceSchedule();
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
//    @Scheduled(cron = "0 */3 * * * *")
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
//    @Scheduled(cron = "0 */3 * * * *") 
    public void createNewSchedule() {
        List<MaintenanceSchedule> listMaintenances = this.mainScheduleRepo.findScheduleToCreateNew();
        for (MaintenanceSchedule maintenance : listMaintenances) {
            if (maintenance.getFrequency() == null || maintenance.getEndDate() == null) {
                continue;
            }

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

            this.mainScheduleRepo.addOrUpdateMaintenanceSchedule(newSchedule);

            MaintenanceAssignment latestAssign = this.mainAssignRepo.getLastestMainAssignByMaintenanceId(maintenance.getId());
            if (latestAssign != null) {
                MaintenanceAssignment newAssign = new MaintenanceAssignment();
                newAssign.setTechnicianId(latestAssign.getTechnicianId());
                newAssign.setMaintenanceScheduleId(newSchedule);
                this.mainAssignRepo.addMaintenanceAssignment(newAssign);
            }
        }
    }

    @Override
    public long countMaintenances(Map<String, String> params) {
        return this.mainScheduleRepo.countMaintenances(params);
    }

    @Override
    //    @Scheduled(cron = "0 */3 * * * *") 
    public void changeMaintenanceStatus() {
        List<MaintenanceSchedule> listMaintenances = this.mainScheduleRepo.findScheduleOverTheTime();
        for (MaintenanceSchedule main : listMaintenances) {
            main.setMaintenanceStatus("Quá hạn bảo trì");
            mainScheduleRepo.addOrUpdateMaintenanceSchedule(main);
        }
    }
}
