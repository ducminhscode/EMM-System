/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Device;
import com.tndm.repositories.DeviceRepository;
import com.tndm.services.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceRepository devRepo;
    
    @Override
    public List<Device> getDevices(Map<String, String> params) {
        return this.devRepo.getDevices(params);
    }

    @Override
    public Device getDeviceById(int id) {
        return this.devRepo.getDeviceById(id);
    }

    @Override
    public Device addOrUpdateDevice(Device d) {
        return this.devRepo.addOrUpdateDevice(d);
    }

    @Override
    public void deleteDevice(int id) {
        this.devRepo.deleteDevice(id);
    }

    @Override
    public long countDevices(Map<String, String> params) {
        return this.devRepo.countDevices(params);
    }

    
    
}
