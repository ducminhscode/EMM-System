/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.RepairHistory;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairHistoryService {

    List<RepairHistory> getRepairHistoriesByProblemId(int problemId);

    List<RepairHistory> getAllRepairHistorires();

}
