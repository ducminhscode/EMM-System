/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.DeviceStatus;
import java.util.List;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface DeviceStatusRepository {

    List<DeviceStatus> getDeviceStatus();
}
