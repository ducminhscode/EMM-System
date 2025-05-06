/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface UserRepository {
    List<User> getAllUser();
    
    List<User> getUsers(Map<String, String> params);
    
    User getUserByUsername(String username);
    
    User getUserById(int id);
    
    User addOrUpdateUser(User u);
    
    void deleteUser(int id);
    
    boolean authenticate(String username, String password);
    
    long countUsers(Map<String, String> params);
    
    long countActiveUsers();
}
