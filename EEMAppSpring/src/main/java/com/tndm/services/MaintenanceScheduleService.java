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

    List<MaintenanceSchedule> getMaintenanceSchedule();

    List<MaintenanceSchedule> getMaintenanceByFacilityIdAndMonth(int facilityId, int month);

    List<MaintenanceSchedule> getMaintenanceScheduleByDeviceId(int deviceId);

    MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m);

    void deleteMaintenanceSchedule(int id);

    MaintenanceSchedule getMaintenanceScheduleById(int id);

    List<MaintenanceSchedule> findSchedulesToNotifyToTechnician(int technicianId, String pageStr);

    void notifySchedule();

    void createNewSchedule();

    void changeMaintenanceStatus();

    long countMaintenances(Map<String, String> params);
}
