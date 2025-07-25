/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.Device;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface DeviceService {

    List<Device> getDevices(Map<String, String> params);

    List<Device> getAllDevices();

    List<Device> getDevicesByTypeId(int typeId);

    List<Device> getDevicesByFacilityId(int facilityId);

    Device getDeviceById(int id);

    Device addOrUpdateDevice(Device d);

    void deleteDevice(int id);

    long countDevices(Map<String, String> params);
    
    long countDevicesActive(Map<String, String> params);
}
