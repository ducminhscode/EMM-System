/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.MaintenanceSchedule;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceScheduleRepository {
    List<MaintenanceSchedule> getMaintenanceSchedule();
    
    MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m);
    
    void deleteMaintenanceSchedule(int id);
    
    MaintenanceSchedule getMaintenanceScheduleById(int id);
    
    List<MaintenanceSchedule> findSchedulesToNotify();
    
    List<MaintenanceSchedule> findScheduleOverTheTime();
}
