/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.User;
import com.tndm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/users")
    public String userView(Model model) {
        model.addAttribute("user", new User());
        return "users";
    }

    @GetMapping("/users/{userId}")
    public String deviceView(Model model, @PathVariable(value = "userId") int id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "users";
    }

    @PostMapping("/users/add")
    public String addOrUpdateUser(@ModelAttribute(value = "user") User u) {
        this.userService.addOrUpdateUser(u);
        return "redirect:/indexUser";
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userId") int id) {
        this.userService.deleteUser(id);
    }

}
