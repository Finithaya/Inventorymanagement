package com.example.inventorymanagement.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.inventorymanagement.model.*;
import com.example.inventorymanagement.repository.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public void saveProduct(Product product) {
        productRepo.save(product);
    }

    public void saveCategory(Category category) {
        categoryRepo.save(category);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}