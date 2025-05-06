/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.Facility;
import com.tndm.repositories.FacilityRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
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
public class FacilityRepositoryImpl implements FacilityRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Facility> getFacilities() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Facility", Facility.class);

        return q.getResultList();
    }

    @Override
    public Facility addOrUpdateFacility(Facility f) {
        Session s = this.factory.getObject().getCurrentSession();
        if (f.getId() == null) {
            s.persist(f);
        } else {
            Facility existingFacility = s.get(Facility.class, f.getId());
            if (existingFacility != null) {
                f.setCreatedDate(existingFacility.getCreatedDate());
            }
            s.merge(f);
        }
        s.refresh(f);
        return f;
    }

    @Override
    public Facility getFacilityById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Facility.class, id);
    }

    @Override
    public void deleteFacility(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Facility p = this.getFacilityById(id);
        s.remove(p);
    }

    @Override
    public long countFacilities(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Facility> root = q.from(Facility.class);
        q.select(b.count(root));

        return s.createQuery(q).getSingleResult();
    }
    
}
