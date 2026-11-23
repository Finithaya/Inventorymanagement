package com.example.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorymanagement.model.PurchaseEntry;

@Repository
public interface PurchaseEntryRepository extends JpaRepository<PurchaseEntry, Long>{
   
}
