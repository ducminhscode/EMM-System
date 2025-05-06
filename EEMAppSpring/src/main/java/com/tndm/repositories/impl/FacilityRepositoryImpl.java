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
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
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

    public static final int PAGE_SIZE = 5;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Facility> getFacilities(Map<String, String> params, boolean paginate) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Facility> q = b.createQuery(Facility.class);
        Root<Facility> root = q.from(Facility.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String searchType = params.get("searchType");
            String value = params.get("value");

            if (searchType != null && value != null && !value.isEmpty()) {
                switch (searchType) {
                    case "name":
                        predicates.add(b.like(root.get("name"), "%" + value + "%"));
                        break;
                    case "address":
                        predicates.add(b.like(root.get("address"), "%" + value + "%"));
                        break;
                }
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(q);

        if (paginate) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;
            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
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
