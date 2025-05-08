/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.formatters;

import com.tndm.pojo.Device;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class DeviceFormatter implements Formatter<Device> {

    @Override
    public String print(Device dev, Locale locale) {
        return String.valueOf(dev.getId());
    }

    @Override
    public Device parse(String devId, Locale locale) throws ParseException {
        Device d = new Device();
        d.setId(Integer.valueOf(devId));
        return d;
    }

}
