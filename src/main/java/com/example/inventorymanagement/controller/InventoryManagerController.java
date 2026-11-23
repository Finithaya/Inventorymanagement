package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/manager/inventory")
public class InventoryManagerController {
    private final ProductRepository productRepository;
    public InventoryManagerController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping
    public String viewInventory(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "stock-manager";	
    }
    @PostMapping("/update")
    public String updateStock(@RequestParam Long productId, @RequestParam int quantityChange) {
       Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
       int newQuantity = product.getQuantity() + quantityChange;
       if (newQuantity  < 0) {
        	newQuantity  = 0;
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);
        return "redirect:/manager/inventory";
    }
}
