/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.RepairType;
import com.tndm.repositories.RepairTypeRepository;
import com.tndm.services.RepairTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class RepairTypeServiceImpl implements RepairTypeService{

    @Autowired
    private RepairTypeRepository repTypeRepo;
    
    @Override
    public RepairType getRepairTypeById(int repairId) {
        return this.repTypeRepo.getRepairTypeById(repairId);
    }

    @Override
    public List<RepairType> getAllRepairType() {
        return this.repTypeRepo.getAllRepairType();
    }
    
}
