/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tndm.repositories;

import com.tndm.pojo.User;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
public interface UserRepository {

    User getUserByUsername(String username);
    
    User addUser(User u);
    
    boolean authenticate(String username, String password);
}
