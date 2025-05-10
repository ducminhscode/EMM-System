/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Technician;
import com.tndm.repositories.TechnicianRepository;
import com.tndm.services.TechnicianService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class TechnicianServiceImpl implements TechnicianService {

    @Autowired
    private TechnicianRepository techRepo;

    @Override
    public List<Technician> getAllTechnician() {
        return this.techRepo.getAllTechnician();
    }

    @Override
    public Technician getTechnicianById(int id) {
        return this.techRepo.getTechnicianById(id);
    }

    @Override
    public List<Technician> getTechnicianByFacilityId(int id) {
        return this.techRepo.getTechnicianByFacilityId(id);
    }

    @Override
    public Technician addOrUpdateTechnician(Technician t) {
        return this.techRepo.addOrUpdateTechnician(t);
    }
}
