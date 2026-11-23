package com.example.inventorymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
    @GetMapping("/admin")
    public String admin(HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin-login";
        }
        return "admin";
    }
    @GetMapping("/admin-login")
    public String adminLoginPage() {
        return "admin-login";
    }
    @PostMapping("/admin-auth")
    public String adminAuthenticate(@RequestParam String username,@RequestParam String password,Model model, HttpSession session) {
       if (username.equals("Fini") && password.equals("123@5")) {
            session.setAttribute("adminUser", username);
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            return "admin-login";
        }
    }
    @GetMapping("/home-reports")
    public String reports(HttpSession session) {
        if (session.getAttribute("adminUser") == null && session.getAttribute("managerUser") == null) {
            return "redirect:/admin-login"; 
        }
        return "redirect:/reports/home";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/logout-success";
    }
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success";
    }
    @GetMapping("/manager-login")
    public String managerLoginPage() {
        return "manager-login";
    }
    @PostMapping("/manager-auth")
    public String managerAuthenticate(@RequestParam String username, @RequestParam String password,Model model, HttpSession session) {
        if (username.equals("Manager") && password.equals("123@5")) {
            session.setAttribute("managerUser", username);
            return "redirect:/manager/dashboard";   
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            return "manager-login";
        }
    }
}
  
 

