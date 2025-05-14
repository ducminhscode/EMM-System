/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.User;
import com.tndm.services.UserService;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.tndm.utils.JwtUtils;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

    //API tạo User
    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestParam Map<String, String> params,
            @RequestParam("avatar") MultipartFile avatar) {
        User u = this.userDetailsService.addUser(params, avatar);

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    //API đăng nhập User
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {

        if (u.getUsername() == null || u.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username hoặc password không được để trống");
        }

        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping(value = "/secure/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userDetailsService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/secure/profile/update")
    @CrossOrigin
    public ResponseEntity<?> updateProfile(
            Principal principal,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        try {
            String username = principal.getName();

            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }

            User updatedUser = new User();
            updatedUser.setId(existingUser.getId());
            updatedUser.setUsername(username);

            updatedUser.setFirstName(params.getOrDefault("firstName", existingUser.getFirstName()));
            updatedUser.setLastName(params.getOrDefault("lastName", existingUser.getLastName()));
            updatedUser.setEmail(params.getOrDefault("email", existingUser.getEmail()));
            updatedUser.setPhone(params.getOrDefault("phone", existingUser.getPhone()));
            updatedUser.setUserRole(existingUser.getUserRole());

            if (avatar != null && !avatar.isEmpty()) {
                updatedUser.setFile(avatar);
            } else {
                updatedUser.setAvatar(existingUser.getAvatar());
            }

            User savedUser = this.userDetailsService.addOrUpdateUser(updatedUser);

            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

    @PostMapping("/secure/profile/change-password")
    @CrossOrigin
    public ResponseEntity<?> changePassword(
            Principal principal,
            @RequestBody Map<String, String> params) {

        try {
            String username = principal.getName();
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy thông tin người dùng đăng nhập");
            }

            String currentPassword = params.get("currentPassword");
            String newPassword = params.get("newPassword");

            if (currentPassword == null || newPassword == null || currentPassword.isEmpty() || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("Mật khẩu cũ và mới không được để trống");
            }

            if (!this.userDetailsService.authenticate(username, currentPassword)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu cũ không đúng");
            }

            User existingUser = this.userDetailsService.getUserByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
            }
            if (existingUser.getId() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dữ liệu người dùng không hợp lệ: ID null");
            }

            User updatedUser = new User();
            updatedUser.setId(existingUser.getId());
            updatedUser.setUsername(username);
            updatedUser.setPassword(newPassword);
            updatedUser.setFirstName(existingUser.getFirstName());
            updatedUser.setLastName(existingUser.getLastName());
            updatedUser.setEmail(existingUser.getEmail());
            updatedUser.setPhone(existingUser.getPhone());
            updatedUser.setAvatar(existingUser.getAvatar());
            updatedUser.setUserRole(existingUser.getUserRole());
            updatedUser.setActive(existingUser.getActive());

            this.userDetailsService.addOrUpdateUser(updatedUser);

            return ResponseEntity.ok().body("Đổi mật khẩu thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi đổi mật khẩu: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> getUsers(@RequestParam Map<String, String> params) {
        try {
            List<User> users = this.userDetailsService.getUsers(params);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

}
