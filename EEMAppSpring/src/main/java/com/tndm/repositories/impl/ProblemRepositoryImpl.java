/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.Problem;
import com.tndm.repositories.ProblemRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class ProblemRepositoryImpl implements ProblemRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Problem> getProblem() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Problem", Problem.class);

        return q.getResultList();
    }

    @Override
    public Problem getProblemById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Problem.class, id);
    }

}
