/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Facility;
import com.tndm.repositories.FacilityRepository;
import com.tndm.services.FacilityService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityRepository faciRepo;

    @Override
    public List<Facility> getFacilities() {
        return this.faciRepo.getFacilities();
    }

    @Override
    public Facility addOrUpdateFacility(Facility f) {
        return this.faciRepo.addOrUpdateFacility(f);
    }

    @Override
    public Facility getFacilityById(int id) {
        return this.faciRepo.getFacilityById(id);
    }

    @Override
    public void deleteFacility(int id) {
        this.faciRepo.deleteFacility(id);
    }

    @Override
    public long countFacilities(Map<String, String> params) {
        return this.faciRepo.countFacilities(params);
    }
}
