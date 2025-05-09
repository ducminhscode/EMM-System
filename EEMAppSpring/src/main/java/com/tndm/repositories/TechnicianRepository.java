/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.Technician;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface TechnicianRepository {
    List<Technician> getAllTechnician();
    
    Technician getTechnicianById(int id);
    
    List<Technician> getTechnicianByFacilityId(int id);
}
