/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.formatters;

import com.tndm.pojo.DeviceType;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public class DeviceTypeFormatter implements Formatter<DeviceType>{

    @Override
    public String print(DeviceType devType, Locale locale) {
        return String.valueOf(devType.getId());
    }

    @Override
    public DeviceType parse(String devTypeId, Locale locale) throws ParseException {
        DeviceType dt = new DeviceType();
        dt.setId(Integer.valueOf(devTypeId));
        return dt;
    }
    
}
