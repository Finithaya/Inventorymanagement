package com.example.inventorymanagement.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.model.Category;
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/manager")
    public String manager(HttpSession session, Model model) {
       if (session.getAttribute("managerUser") == null) {
            return "redirect:/manager-login";
        }
        model.addAttribute("category", new Category());
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "manage-products";
    }
    @GetMapping("/profile")
    public String profilePage() {
        return "admin-profile";
    }
   @GetMapping("/edit-profile")
    public String editProfilePage(Model model) {
        model.addAttribute("name", "Fini T S");
        model.addAttribute("email", "admin@gmail.com");
        model.addAttribute("phone", "9876543210");
        return "edit-profile";
    }
    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam String name,@RequestParam String email,@RequestParam String phone) {
        return "redirect:/admin/profile";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }
   @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
    
}