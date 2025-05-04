/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Problem;
import com.tndm.repositories.ProblemRepository;
import com.tndm.services.ProblemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ProblemServiceImpl implements ProblemService{
    
    @Autowired
    private ProblemRepository problemService;

    @Override
    public List<Problem> getProblem() {
        return this.problemService.getProblem();
    }

    @Override
    public Problem getProblemById(int id) {
        return this.problemService.getProblemById(id);
    }
    
}
