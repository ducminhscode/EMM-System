/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tndm.pojo.User;
import com.tndm.repositories.UserRepository;
import com.tndm.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Service("userDetailsService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository usrRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByUsername(String username) {
        return this.usrRepo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");

        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    //API tạo User
    @Override
    public User addUser(Map<String, String> params, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setEmail(params.get("email"));
        u.setPhone(params.get("phone"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setUserRole("ROLE_ADMIN");
        u.setActive(Boolean.TRUE);

        if (!avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "BaoTriThietBi"
                ));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.usrRepo.addUser(u);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.usrRepo.authenticate(username, password);
    }

    @Override
    public User addOrUpdateUser(User u) {
        if (u.getId() != null) {
            User existingUser = usrRepo.getUserById(u.getId());

            if (u.getPassword() == null || u.getPassword().isEmpty()) {
                u.setPassword(existingUser.getPassword());
            } else if (!u.getPassword().equals(existingUser.getPassword())) {
                u.setPassword(this.passwordEncoder.encode(u.getPassword()));
            }

            if (u.getAvatar() == null) {
                u.setAvatar(existingUser.getAvatar());
            }
        } else {
            u.setPassword(this.passwordEncoder.encode("123"));
        }

        if (u.getFile() != null && !u.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(u.getFile().getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "BaoTriThietBi"
                ));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            u.setAvatar("https://res.cloudinary.com/dp9b0dkkt/image/upload/v1745512749/de995be2-6311-4125-9ac2-19e11fcaf801_jo8gcs.png");
        }

        return this.usrRepo.addOrUpdateUser(u);
    }

    @Override
    public void deleteUser(int id
    ) {
        this.usrRepo.deleteUser(id);
    }

    @Override
    public User getUserById(int id
    ) {
        return this.usrRepo.getUserById(id);
    }

    @Override
    public List<User> getUsers(Map<String, String> params
    ) {
        return this.usrRepo.getUsers(params);
    }

    @Override
    public long countUsers(Map<String, String> params
    ) {
        return this.usrRepo.countUsers(params);
    }

    @Override
    public long countActiveUsers() {
        return this.usrRepo.countActiveUsers();
    }
    
}
