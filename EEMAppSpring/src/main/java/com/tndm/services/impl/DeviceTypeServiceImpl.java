/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.DeviceType;
import com.tndm.repositories.DeviceTypeRepository;
import com.tndm.services.DeviceTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService{

    @Autowired
    private DeviceTypeRepository deviceTypeRepo;
    
    @Override
    public List<DeviceType> getDeviceTypes() {
        return this.deviceTypeRepo.getDeviceTypes();
    }
    
}
