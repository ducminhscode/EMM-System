/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.Facility;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface FacilityService {

    List<Facility> getFacilities();
    
    Facility addOrUpdateFacility(Facility d);
    
    Facility getFacilityById(int id);
    
    void deleteFacility(int id);
    
    long countFacilities(Map<String, String> params);

}
