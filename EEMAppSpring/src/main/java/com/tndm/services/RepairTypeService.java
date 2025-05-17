/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.RepairType;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairTypeService {

    RepairType getRepairTypeById(int repairId);

    List<RepairType> getAllRepairType();
}
