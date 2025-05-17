/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.Problem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ProblemService {

    List<Problem> getProblem(Map<String, String> params);

    Problem getProblemById(int id);

    Problem addOrUpdateProblem(Problem p);

    List<Problem> getProblemsByTechnicianId(int technicianId);

    long countProblems(Map<String, String> params);

    void deleteProblem(int id);

    List<Problem> getProblemsByDeviceIds(List<Integer> deviceIds);

}
