/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.formatters;

import com.tndm.pojo.DeviceStatus;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public class DeviceStatusFormatter implements Formatter<DeviceStatus>{

    @Override
    public String print(DeviceStatus devStatus, Locale locale) {
        return String.valueOf(devStatus.getId());
    }

    @Override
    public DeviceStatus parse(String devStatusId, Locale locale) throws ParseException {
        DeviceStatus dt = new DeviceStatus();
        dt.setId(Integer.valueOf(devStatusId));
        return dt;
    }
    
}
