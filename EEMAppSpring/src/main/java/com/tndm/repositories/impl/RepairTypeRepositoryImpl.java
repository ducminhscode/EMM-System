/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.RepairType;
import com.tndm.repositories.RepairTypeRepository;
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
public class RepairTypeRepositoryImpl implements RepairTypeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public RepairType getRepairTypeById(int repairId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(RepairType.class, repairId);
    }

    @Override
    public List<RepairType> getAllRepairType() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM RepairType", RepairType.class);

        return q.getResultList();
    }

}
