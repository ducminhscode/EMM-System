/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.RepairHistory;
import com.tndm.repositories.RepairHistoryRepository;
import com.tndm.services.RepairHistoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class RepairHistoryServiceImpl implements RepairHistoryService{
    @Autowired
    private RepairHistoryRepository repairHistoryRepo;

    @Override
    public List<RepairHistory> getRepairHistoriesByProblemId(int problemId) {
        return this.repairHistoryRepo.getRepairHistoriesByProblemId(problemId);
    }
    
}
