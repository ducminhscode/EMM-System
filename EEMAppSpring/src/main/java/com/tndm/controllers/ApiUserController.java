/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.User;
import com.tndm.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

//    @PostMapping("/users")
//    public ResponseEntity<User> create(@RequestParam Map<String, String> params,
//            @RequestParam("avatar") MultipartFile avatar) {
//        User u = this.userDetailsService.addUser(params, avatar);
//
//        return new ResponseEntity<>(u, HttpStatus.CREATED);
//    }
    
    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userId") int id) {
        this.userDetailsService.deleteUser(id);
    }
    
}
