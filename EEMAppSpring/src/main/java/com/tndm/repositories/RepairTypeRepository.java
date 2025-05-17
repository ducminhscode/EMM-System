/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.RepairType;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairTypeRepository {

    RepairType getRepairTypeById(int repairId);

    List<RepairType> getAllRepairType();
}
