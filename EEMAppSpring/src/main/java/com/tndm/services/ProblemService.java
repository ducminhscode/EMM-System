/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.Problem;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface ProblemService {
    List<Problem> getProblem();
    
    Problem getProblemById(int id);
}
