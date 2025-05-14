/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.Problem;
import com.tndm.repositories.ProblemRepository;
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
 * @author ADMIN
 */
@Repository
@Transactional
public class ProblemRepositoryImpl implements ProblemRepository {

    public static final int PAGE_SIZE = 5;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Problem> getProblem(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Problem> q = b.createQuery(Problem.class);
        Root<Problem> root = q.from(Problem.class);
        q.select(root);

        Query query = s.createQuery(q);

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * PAGE_SIZE;
        query.setMaxResults(PAGE_SIZE);
        query.setFirstResult(start);

        return query.getResultList();
    }

    @Override
    public Problem getProblemById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Problem.class, id);
    }

    @Override
    public long countProblems(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Problem> root = q.from(Problem.class);
        q.select(b.count(root));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public void deleteProblem(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Problem p = this.getProblemById(id);
        s.remove(p);
    }

    @Override
    public List<Problem> getProblemsByDeviceIds(List<Integer> deviceIds) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM Problem p WHERE p.deviceId.id IN (:deviceIds)";
        return s.createQuery(hql, Problem.class)
                .setParameter("deviceIds", deviceIds)
                .list();
    }

    @Override
    public Problem updateProblem(Problem p) {
        Session s = this.factory.getObject().getCurrentSession();
        if (p.getId() == null) {
            s.persist(p);
        } else {
            s.merge(p);
        }
        s.refresh(p);
        return p;
    }
}
