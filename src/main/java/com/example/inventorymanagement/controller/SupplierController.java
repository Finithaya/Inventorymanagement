package com.example.inventorymanagement.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventorymanagement.model.Supplier;
import com.example.inventorymanagement.repository.SupplierRepository;

@Controller
@RequestMapping("/admin/suppliers")
public class SupplierController {
	    private final SupplierRepository supplierRepository;
        public SupplierController(SupplierRepository supplierRepository) {
	        this.supplierRepository = supplierRepository;
	    }
        @GetMapping
	    public String viewSuppliers(Model model) {
	        model.addAttribute("supplier", new Supplier());
	        model.addAttribute("suppliers", supplierRepository.findAll());
	        return "supplier-management";
	    }
        @PostMapping("/save")
	    public String saveSupplier(@ModelAttribute Supplier supplier) {
	        supplierRepository.save(supplier);
	        return "redirect:/admin/suppliers";
	    }
        @GetMapping("/delete/{id}")
	    public String deleteSupplier(@PathVariable Long id) {
	        supplierRepository.deleteById(id);
	        return "redirect:/admin/suppliers";
	    }
        @GetMapping("/edit/{id}")
	    public String editSupplier(@PathVariable Long id, Model model) {
	        Supplier supplier = supplierRepository.findById(id).orElseThrow();
	        model.addAttribute("supplier", supplier);
	        model.addAttribute("suppliers", supplierRepository.findAll());
	        return "supplier-management";
	    }
        @GetMapping("/search")
	    public String searchSupplier(@RequestParam String keyword, Model model) {
	        List<Supplier> suppliers =
	                supplierRepository.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("supplier", new Supplier());
	        model.addAttribute("suppliers", suppliers);
	        return "supplier-management";
	    }
}
