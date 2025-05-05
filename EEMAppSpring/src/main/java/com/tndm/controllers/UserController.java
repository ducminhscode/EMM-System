/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tndm.controllers;

import com.tndm.pojo.User;
import com.tndm.services.MailService;
import com.tndm.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Tran Nguyen Duc Minh
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

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
    public String userDetailView(Model model, @PathVariable(value = "userId") int id) {
        model.addAttribute("user", this.userService.getUserById(id));
        return "users";
    }

    @PostMapping("/users/add")
    public String addOrUpdateUser(@ModelAttribute(value = "user") User u) {
        this.userService.addOrUpdateUser(u);
        mailService.sendMail(u.getEmail(),
                "Thông báo tài khoản của bạn",
                "Chào " + u.getFirstName() + u.getLastName() + ",\n\n"
                + "Tài khoản của bạn đã được tạo.\n"
                + "Tên tài khoản: " + u.getUsername() + "\n"
                + "Mật khẩu: 123" + "\n\n"
                + "Trân trọng,\n"
                + "Đội ngũ Admin.");
        return "redirect:/index-users";
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "userId") int id) {
        this.userService.deleteUser(id);
    }

    @GetMapping("/profile")
    public String profileView(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        // Cập nhật thông tin từ form vào currentUser
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPhone(updatedUser.getPhone());

        // Xử lý avatar nếu có
        if (updatedUser.getFile() != null && !updatedUser.getFile().isEmpty()) {
            // Logic upload avatar (đã có trong service)
        }

        userService.addOrUpdateUser(currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới không khớp!");
            return "redirect:/profile";
        }

        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        if (!userService.authenticate(username, currentPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu hiện tại không đúng!");
            return "redirect:/profile";
        }

        user.setPassword(newPassword);
        userService.addOrUpdateUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        return "redirect:/profile";
    }

}
