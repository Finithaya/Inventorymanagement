package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.SupplierRepository;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReportController {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;

    public ReportController(ProductRepository productRepository,
                            SupplierRepository supplierRepository,
                            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.categoryRepository = categoryRepository;
    }
    @GetMapping("/admin/reports")
    public String viewReports(Model model) {
        long totalProducts = productRepository.count();
        long totalSuppliers = supplierRepository.count();
        long totalCategories = categoryRepository.count();
        List<Product> lowStockProducts =productRepository.findByQuantityLessThanEqual(10);
        double totalInventoryValue = productRepository.findAll().stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalSuppliers", totalSuppliers);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("totalInventoryValue", totalInventoryValue);
        return "view-reports";
    }
}