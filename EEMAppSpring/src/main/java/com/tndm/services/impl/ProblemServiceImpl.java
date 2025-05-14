/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.tndm.pojo.Problem;
import com.tndm.repositories.ProblemRepository;
import com.tndm.services.ProblemService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemRepository problemService;

    @Override
    public List<Problem> getProblem(Map<String, String> params) {
        return this.problemService.getProblem(params);
    }

    @Override
    public Problem getProblemById(int id) {
        return this.problemService.getProblemById(id);
    }

    @Override
    public long countProblems(Map<String, String> params) {
        return this.problemService.countProblems(params);
    }

    @Override
    public void deleteProblem(int id) {
        this.problemService.deleteProblem(id);
    }

    @Override
    public List<Problem> getProblemsByDeviceIds(List<Integer> deviceIds) {
        return problemService.getProblemsByDeviceIds(deviceIds);
    }

    @Override
    public Problem updateProblem(Problem p) {
        return problemService.updateProblem(p);
    }
}
