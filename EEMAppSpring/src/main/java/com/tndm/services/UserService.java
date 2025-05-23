/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.services;

import com.tndm.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface UserService extends UserDetailsService {
    
    List<User> getUsers(Map<String, String> params);
    
    User addOrUpdateUser(User u);

    User getUserById(int id);
    
    User getUserByUsername(String username);

    void deleteUser(int id);
    
    boolean authenticate(String username, String password);
    
    long countUsers(Map<String, String> params);
    
    long countActiveUsers();
    
}
