/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.MaintenanceSchedule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceScheduleService {

    List<MaintenanceSchedule> getMaintenanceSchedule(Map<String, String> params);

    List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndTime(int deviceId, int month, int year);

    List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndYear(int deviceId, int year);

    MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m);

    void deleteMaintenanceSchedule(int id);

    MaintenanceSchedule getMaintenanceScheduleById(int id);

    List<MaintenanceSchedule> findSchedulesToNotifyToTechnician(int technicianId, String pageStr);

    MaintenanceSchedule findTheLastestScheduleByDeviceId(int deviceId);

    void notifySchedule();

    void createNewSchedule();

    void changeMaintenanceStatus();

    long countMaintenances(Map<String, String> params);
}
