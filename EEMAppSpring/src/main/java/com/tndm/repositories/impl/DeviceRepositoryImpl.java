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

    public static final int PAGE_SIZE = 5;

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

            String searchType = params.get("searchType");
            String value = params.get("value");

            if (searchType != null && value != null && !value.isEmpty()) {
                switch (searchType) {
                    case "name":
                        predicates.add(b.like(root.get("name"), "%" + value + "%"));
                        break;
                    case "manufacturer":
                        predicates.add(b.like(root.get("manufacturer"), "%" + value + "%"));
                        break;
                }
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(q);

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * PAGE_SIZE;
        query.setMaxResults(PAGE_SIZE);
        query.setFirstResult(start);

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

    @Override
    public long countDevices(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Device> root = q.from(Device.class);
        q.select(b.count(root));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public List<Device> getAllDevices() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Device", Device.class);

        return q.getResultList();
    }

    @Override
    public List<Device> getDevicesByTypeId(int typeId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM Device d WHERE d.typeId.id = :typeId";
        return s.createQuery(hql, Device.class)
                .setParameter("typeId", typeId)
                .list();
    }

    @Override
    public List<Device> getDevicesByFacilityId(int facilityId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM Device d WHERE d.facilityId.id = :facilityId";
        return s.createQuery(hql, Device.class)
                .setParameter("facilityId", facilityId)
                .list();
    }
}
