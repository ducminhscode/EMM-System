/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.FatalLevel;
import com.tndm.repositories.FatalLevelRepository;
import com.tndm.services.FatalLevelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class FatalLevelServiceImpl implements FatalLevelService {

    @Autowired
    private FatalLevelRepository fatalRepo;

    @Override
    public List<FatalLevel> getFatalLevel() {
        return this.fatalRepo.getFatalLevel();
    }

    @Override
    public FatalLevel getFatalLevelById(int id) {
        return this.fatalRepo.getFatalLevelById(id);
    }
}
