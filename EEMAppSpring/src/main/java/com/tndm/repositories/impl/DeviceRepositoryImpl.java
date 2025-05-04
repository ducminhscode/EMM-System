/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.Device;
import com.tndm.repositories.DeviceRepository;
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
public class DeviceRepositoryImpl implements DeviceRepository {

    private static final int PAGE_SIZE = 6;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Device> getDevices(Map<String, String> params) {

        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Device> q = b.createQuery(Device.class);
        Root<Device> root = q.from(Device.class);
        q.select(root);

        if (params != null) {

            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("name") && !params.get("name").isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", params.get("name"))));
            }

            if (params.containsKey("type") && !params.get("type").isEmpty()) {
                predicates.add(b.equal(root.get("type"), params.get("type")));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;
            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Device getDeviceById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Device.class, id);
    }

    @Override
    public Device addOrUpdateDevice(Device d) {
        Session s = this.factory.getObject().getCurrentSession();
        if (d.getId() == null) {
            s.persist(d);
        } else {
            Device existingDevice = s.get(Device.class, d.getId());
            if (existingDevice != null) {
                d.setCreatedDate(existingDevice.getCreatedDate());
            }
            s.merge(d);
        }
        s.refresh(d);
        return d;
    }

    @Override
    public void deleteDevice(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Device p = this.getDeviceById(id);
        s.remove(p);
    }

}
