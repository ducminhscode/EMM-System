/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.MaintenanceType;
import com.tndm.repositories.MaintenanceTypeRepository;
import com.tndm.services.MaintenanceTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class MaintenanceTypeServiceImpl implements MaintenanceTypeService{

    @Autowired
    private MaintenanceTypeRepository mainTypeRepo;
    
    @Override
    public List<MaintenanceType> getMaintenanceTypes() {
        return this.mainTypeRepo.getMaintenanceTypes();
    }

    @Override
    public MaintenanceType getMaintenaceTypeByName(String name) {
        return this.mainTypeRepo.getMaintenaceTypeByName(name);
    }
    
}
