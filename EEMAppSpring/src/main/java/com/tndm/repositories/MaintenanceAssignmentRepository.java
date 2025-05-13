/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.MaintenanceAssignment;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceAssignmentRepository {

    List<MaintenanceAssignment> getAssignmentByMaintenanceId(int maintenanceId);

    List<MaintenanceAssignment> getAssignmentByTechnicianId(int technicianId);

    MaintenanceAssignment getMaintenanceAssignmentById(int id);

    MaintenanceAssignment addMaintenanceAssignment(MaintenanceAssignment m);

    MaintenanceAssignment getLastestMainAssignByMaintenanceId(int maintenanceId);

    void deleteMaintenanceAssignment(int id);
}
