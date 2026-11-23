package com.example.inventorymanagement.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.inventorymanagement.model.*;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public String viewProducts(Model model) {
        model.addAttribute("products", service.getAllProducts());
        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("product", new Product());
        model.addAttribute("category", new Category());
        return "manage-products";
    }
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        Category category = categoryRepository .findById(product.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        productRepository.save(product);
        return "redirect:/admin/products";
    }
    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category) {
        service.saveCategory(category);
        return "redirect:/admin/products";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/admin/products";
    }
}