/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.repositories.impl;

import com.tndm.pojo.User;
import com.tndm.repositories.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    public static final int PAGE_SIZE = 5;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        return (User) q.getSingleResult();
    }

    //API tạo User
    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();

        s.persist(u);

        s.refresh(u);

        return u;
    }
    
    
    @Override
    public User addOrUpdateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u.getId() == null) {
            s.persist(u);
        } else {
            User existingUser = s.get(User.class, u.getId());
            if (existingUser != null) {
                u.setCreatedDate(existingUser.getCreatedDate());
            }
            s.merge(u);
        }
        s.refresh(u);

        return u;
    }

    @Override
    public boolean authenticate(String username, String password
    ) {
        User u = this.getUserByUsername(username);

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<User> getAllUser() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User", User.class);

        return q.getResultList();
    }

    @Override
    public void deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = this.getUserById(id);
        s.remove(u);

    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String searchType = params.get("searchType");
            String value = params.get("value");

            if (searchType != null && value != null && !value.isEmpty()) {
                switch (searchType) {
                    case "name":
                        Predicate firstNamePredicate = b.like(root.get("firstName"), "%" + value + "%");
                        Predicate lastNamePredicate = b.like(root.get("lastName"), "%" + value + "%");
                        predicates.add(b.or(firstNamePredicate, lastNamePredicate));
                        break;
                    case "username":
                        predicates.add(b.like(root.get("username"), "%" + value + "%"));
                        break;
                    case "email":
                        predicates.add(b.like(root.get("email"), "%" + value + "%"));
                        break;
                    case "phone":
                        predicates.add(b.like(root.get("phone"), "%" + value + "%"));
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
    public long countUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.count(root));

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public long countActiveUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);

        q.select(b.count(root)).where(b.equal(root.get("active"), true));

        return s.createQuery(q).getSingleResult();
    }

}
