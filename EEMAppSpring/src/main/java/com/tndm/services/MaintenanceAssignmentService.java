/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.MaintenanceAssignment;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceAssignmentService {

    List<MaintenanceAssignment> getAssignmentByMaintenanceId(int maintenanceId);

    List<MaintenanceAssignment> getAssignmentByTechnicianId(int technicianId);

    MaintenanceAssignment addMaintenanceAssignment(MaintenanceAssignment m);

    MaintenanceAssignment getLastestMainAssignByMaintenanceId(int maintenanceId);

}
