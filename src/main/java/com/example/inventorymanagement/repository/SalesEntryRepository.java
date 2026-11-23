package com.example.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorymanagement.model.SalesEntry;

@Repository
public interface SalesEntryRepository extends JpaRepository<SalesEntry, Long>{

}
