/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.MaintenanceSchedule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceScheduleRepository {

    List<MaintenanceSchedule> getMaintenanceSchedule(Map<String, String> params);

    List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndTime(int deviceId, int month, int year);

    List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndYear(int deviceId, int year);

    MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m);

    void deleteMaintenanceSchedule(int id);

    MaintenanceSchedule getMaintenanceScheduleById(int id);

    List<MaintenanceSchedule> findSchedulesToNotifyToTechnician(int technicianId, String pageStr);

    List<MaintenanceSchedule> findSchedulesToNotify();

    List<MaintenanceSchedule> findScheduleOverTheTime();
    
    MaintenanceSchedule findTheLastestScheduleByDeviceId(int deviceId);

    long countMaintenances(Map<String, String> params);
}
