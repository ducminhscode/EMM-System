/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.repositories.MaintenanceScheduleRepository;
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
public class MaintenanceScheduleRepositoryImpl implements MaintenanceScheduleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MaintenanceSchedule> getMaintenanceSchedule() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM MaintenanceSchedule", MaintenanceSchedule.class);

        return q.getResultList();
    }

    @Override
    public MaintenanceSchedule addOrUpdateMaintenanceSchedule(MaintenanceSchedule m) {
        Session s = this.factory.getObject().getCurrentSession();
        if (m.getId() == null) {
            s.persist(m);
        } else {
            s.merge(m);
        }
        s.refresh(m);
        return m;
    }

    @Override
    public void deleteMaintenanceSchedule(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        MaintenanceSchedule m = this.getMaintenanceScheduleById(id);
        s.remove(m);
    }

    @Override
    public MaintenanceSchedule getMaintenanceScheduleById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(MaintenanceSchedule.class, id);
    }

    @Override
    public List<MaintenanceSchedule> findSchedulesToNotify() {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceSchedule WHERE endDate < CURRENT_DATE "
                + "OR (endDate > CURRENT_DATE AND maintenanceStatus = 'Chưa bảo trì')";
        Query<MaintenanceSchedule> q = s.createQuery(hql, MaintenanceSchedule.class);

        return q.getResultList();
    }

}
