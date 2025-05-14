/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.RepairHistory;
import com.tndm.repositories.RepairHistoryRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class RepairHistoryRepositoryImpl implements RepairHistoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<RepairHistory> getRepairHistoriesByProblemId(int problemID) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM RepairHistory rh WHERE rh.problemId.id = :problemId";
        return s.createQuery(hql, RepairHistory.class)
                .setParameter("problemId", problemID)
                .list();
    }

    @Override
    public List<RepairHistory> getAllRepairHistorires() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM RepairHistory", RepairHistory.class);

        return q.getResultList();
    }
}
