/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.repositories.MaintenanceAssignmentRepository;
import com.tndm.services.MaintenanceAssignmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class MaintenanceAssignmentServiceImpl implements MaintenanceAssignmentService{

    @Autowired
    private MaintenanceAssignmentRepository mainAssignmentRepo;
    
    @Override
    public List<MaintenanceAssignment> getAssignmentByMaintenanceId(int maintenanceId) {
        return this.mainAssignmentRepo.getAssignmentByMaintenanceId(maintenanceId);
    }

    @Override
    public List<MaintenanceAssignment> getAssignmentByTechnicianId(int technicianId) {
        return this.mainAssignmentRepo.getAssignmentByTechnicianId(technicianId);
    }

    @Override
    public MaintenanceAssignment addMaintenanceAssignment(MaintenanceAssignment m) {
        return this.mainAssignmentRepo.addMaintenanceAssignment(m);
    }
    
}
