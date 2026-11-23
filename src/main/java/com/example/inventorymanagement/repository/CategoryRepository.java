package com.example.inventorymanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorymanagement.model.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	  
}
