/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.ProblemStatus;
import com.tndm.repositories.ProblemStatusRepository;
import com.tndm.services.ProblemStatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ProblemStatusServiceImpl implements ProblemStatusService{

    @Autowired
    private ProblemStatusRepository problemStatusRepo;
    
    @Override
    public List<ProblemStatus> getProblemStatus() {
        return this.problemStatusRepo.getProblemStatus();
    }

    @Override
    public ProblemStatus getProblemStatusByName(String name) {
        return this.problemStatusRepo.getProblemStatusByName(name);
    }
    
}
