/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.MaintenanceType;
import com.tndm.repositories.MaintenanceTypeRepository;
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
public class MaintenanceTypeRepositoryImpl implements MaintenanceTypeRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<MaintenanceType> getMaintenanceTypes() {
                
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM MaintenanceType", MaintenanceType.class);

        return q.getResultList();
    }
    
}
