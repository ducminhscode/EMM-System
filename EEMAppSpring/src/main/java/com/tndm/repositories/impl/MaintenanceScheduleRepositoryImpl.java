/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.MaintenanceSchedule;
import com.tndm.repositories.MaintenanceScheduleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static final int PAGE_SIZE = 5;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MaintenanceSchedule> getMaintenanceSchedule(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<MaintenanceSchedule> q = b.createQuery(MaintenanceSchedule.class);
        Root<MaintenanceSchedule> root = q.from(MaintenanceSchedule.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String searchType = params.get("searchType");
            String value = params.get("value");

            if (searchType != null && value != null && !value.isEmpty()) {
                switch (searchType) {
                    case "title":
                        predicates.add(b.like(root.get("title"), "%" + value + "%"));
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
        String hql = "FROM MaintenanceSchedule "
                + "WHERE maintenanceStatus = 'Chưa bảo trì' "
                + "OR maintenanceStatus = 'Quá hạn bảo trì'";
        Query<MaintenanceSchedule> q = s.createQuery(hql, MaintenanceSchedule.class);

        return q.getResultList();
    }


    @Override
    public List<MaintenanceSchedule> findScheduleOverTheTime() {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceSchedule WHERE endDate < CURRENT_DATE " + "AND maintenanceStatus = 'Chưa bảo trì'" + "AND maintenanceStatus != 'Đang bảo trì'";
        Query<MaintenanceSchedule> q = s.createQuery(hql, MaintenanceSchedule.class);
        return q.getResultList();
    }

    @Override
    public long countMaintenances(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<MaintenanceSchedule> root = q.from(MaintenanceSchedule.class);
        q.select(b.count(root));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public List<MaintenanceSchedule> findSchedulesToNotifyToTechnician(int technicianId, String pageStr) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT ma.maintenanceScheduleId FROM MaintenanceAssignment ma "
                + "WHERE ma.technicianId.id = :technicianId "
                + "AND ma.maintenanceScheduleId.maintenanceStatus = 'Chưa bảo trì'"
                + "OR ma.maintenanceScheduleId.maintenanceStatus = 'Đang bảo trì'"
                + "OR ma.maintenanceScheduleId.maintenanceStatus = 'Quá hạn bảo trì'";

        Query query = s.createQuery(hql, MaintenanceSchedule.class).setParameter("technicianId", technicianId);

        int page = 1;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException ex) {

        }

        int start = (page - 1) * PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(PAGE_SIZE);

        return query.getResultList();
    }

    @Override
    public List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndTime(int deviceId, int month, int year) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceSchedule ms "
                + "WHERE ms.deviceId.id = :deviceId "
                + "AND MONTH(ms.maintenanceDate) = :month "
                + "AND YEAR(ms.maintenanceDate) = :year "
                + "AND ms.maintenanceStatus = 'Đã bảo trì'";

        Query<MaintenanceSchedule> query = s.createQuery(hql, MaintenanceSchedule.class);
        query.setParameter("deviceId", deviceId);
        query.setParameter("month", month);
        query.setParameter("year", year);

        return query.getResultList();
    }

    @Override
    public List<MaintenanceSchedule> getMaintenanceScheduleByDeviceIdAndYear(int deviceId, int year) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM MaintenanceSchedule ms "
                + "WHERE ms.deviceId.id = :deviceId "
                + "AND YEAR(ms.maintenanceDate) = :year "
                + "AND ms.maintenanceStatus = 'Đã bảo trì'";

        Query<MaintenanceSchedule> query = s.createQuery(hql, MaintenanceSchedule.class);
        query.setParameter("deviceId", deviceId);
        query.setParameter("year", year);

        return query.getResultList();
    }

    @Override
    public MaintenanceSchedule findTheLastestScheduleByDeviceId(int deviceId) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "FROM MaintenanceSchedule "
                + "WHERE deviceId.id = :deviceId "
                + "AND frequency != '' "
                + "ORDER BY id DESC";

        Query<MaintenanceSchedule> q = s.createQuery(hql, MaintenanceSchedule.class);
        q.setParameter("deviceId", deviceId);
        q.setMaxResults(1);

        return q.uniqueResult();
    }
}
