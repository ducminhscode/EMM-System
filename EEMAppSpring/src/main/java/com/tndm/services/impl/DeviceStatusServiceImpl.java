/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.DeviceStatus;
import com.tndm.repositories.DeviceStatusRepository;
import com.tndm.services.DeviceStatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService{

    @Autowired
    private DeviceStatusRepository deviceStatusRepo;
    
    @Override
    public List<DeviceStatus> getDeviceStatus() {
        return this.deviceStatusRepo.getDeviceStatus();
    }
    
}
