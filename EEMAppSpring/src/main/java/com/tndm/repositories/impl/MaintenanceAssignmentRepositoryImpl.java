/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.MaintenanceAssignment;
import com.tndm.repositories.MaintenanceAssignmentRepository;
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
public class MaintenanceAssignmentRepositoryImpl implements MaintenanceAssignmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MaintenanceAssignment> getAssignmentByMaintenanceId(int maintenanceId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceAssignment ma WHERE ma.maintenanceScheduleId.id = :maintenanceId";
        return s.createQuery(hql, MaintenanceAssignment.class)
                .setParameter("maintenanceId", maintenanceId)
                .list();
    }

    @Override
    public List<MaintenanceAssignment> getAssignmentByTechnicianId(int technicianId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceAssignment ma WHERE ma.technicianId.id = :technicianId";
        return s.createQuery(hql, MaintenanceAssignment.class)
                .setParameter("technicianId", technicianId)
                .list();
    }

    @Override
    public MaintenanceAssignment addMaintenanceAssignment(MaintenanceAssignment m) {
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
    public MaintenanceAssignment getLastestMainAssignByMaintenanceId(int maintenanceId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceAssignment ma WHERE ma.maintenanceId.id = :maintenanceId ORDER BY ma.id DESC";
        return s.createQuery(hql, MaintenanceAssignment.class)
                .setParameter("maintenanceId", maintenanceId)
                .setMaxResults(1)
                .uniqueResult();
    }

}
