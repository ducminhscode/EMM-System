/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.RepairHistory;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairHistoryRepository {

    List<RepairHistory> getRepairHistoriesByProblemId(int problemId);

    List<RepairHistory> getAllRepairHistorires();
    
    RepairHistory getRepairHistoryByProblemIdAndTechnicianId(int problemId, int technicianId);
    
    RepairHistory addOrUpdateRepairHistory(RepairHistory r);
}
