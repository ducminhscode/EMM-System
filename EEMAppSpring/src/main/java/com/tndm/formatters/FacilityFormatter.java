/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.formatters;

import com.tndm.pojo.Facility;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public class FacilityFormatter implements Formatter<Facility>{

    @Override
    public String print(Facility faci, Locale locale) {
        return String.valueOf(faci.getId());
    }

    @Override
    public Facility parse(String faciId, Locale locale) throws ParseException {
        Facility f = new Facility();
        f.setId(Integer.valueOf(faciId));
        return f;
    }
    
}
