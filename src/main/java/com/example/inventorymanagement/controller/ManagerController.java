package com.example.inventorymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
    	if (session.getAttribute("managerUser") == null) {
            return "redirect:/manager-login";
        }
        return "manager-dashboard";
     }
 }