/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.Technician;
import com.tndm.repositories.TechnicianRepository;
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
public class TechnicianRepositoryImpl implements TechnicianRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Technician> getAllTechnician() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Technician", Technician.class);

        return q.getResultList();
    }

    @Override
    public Technician getTechnicianById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Technician.class, id);
    }

    @Override
    public List<Technician> getTechnicianByFacilityId(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM Technician t WHERE t.facilityId.id = :facilityId";
        return s.createQuery(hql, Technician.class)
                .setParameter("facilityId", id)
                .list();

    }

    @Override
    public Technician addOrUpdateTechnician(Technician t) {
        Session s = this.factory.getObject().getCurrentSession();
        if (t.getId() == null) {
            s.persist(t);
        } else {
            s.merge(t);
        }
        return t;
    }
}
