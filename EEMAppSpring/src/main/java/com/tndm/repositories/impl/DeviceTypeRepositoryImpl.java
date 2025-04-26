/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.DeviceType;
import com.tndm.repositories.DeviceTypeRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Repository
@Transactional
public class DeviceTypeRepositoryImpl implements DeviceTypeRepository{

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<DeviceType> getDeviceTypes() {
        
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM DeviceType", DeviceType.class);

        return q.getResultList();
    }
    
}
