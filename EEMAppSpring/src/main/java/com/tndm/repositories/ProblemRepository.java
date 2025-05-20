/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.Problem;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ProblemRepository {

    List<Problem> getProblem(Map<String, String> params);

    List<Problem> getProblemsByDeviceIds(List<Integer> deviceIds);

    List<Problem> getProblemsByTechnicianId(int technicianId, String pageStr);
    
    Problem getProblemById(int id);

    Problem addOrUpdateProblem(Problem p);

    long countProblems(Map<String, String> params);

    void deleteProblem(int id);
}
