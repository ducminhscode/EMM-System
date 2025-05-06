/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.Facility;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface FacilityRepository {
    
    List<Facility> getFacilities(Map<String, String> params, boolean paginate);
    
    Facility addOrUpdateFacility(Facility f);
    
    Facility getFacilityById(int id);
    
    void deleteFacility(int id);
    
    long countFacilities(Map<String, String> params); 
}
