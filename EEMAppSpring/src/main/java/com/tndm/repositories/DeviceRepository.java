/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.Device;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface DeviceRepository {

    List<Device> getDevices(Map<String, String> params);

    Device getDeviceById(int id);

    Device addOrUpdateDevice(Device d);

    void deleteDevice(int id);
}
