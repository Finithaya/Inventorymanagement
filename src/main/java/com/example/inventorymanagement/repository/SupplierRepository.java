package com.example.inventorymanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorymanagement.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
	  List<Supplier> findByNameContainingIgnoreCase(String name);
}
