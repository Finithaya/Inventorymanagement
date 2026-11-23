package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.model.PurchaseEntry;
import com.example.inventorymanagement.model.SalesEntry;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.PurchaseEntryRepository;
import com.example.inventorymanagement.repository.SalesEntryRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manager")
public class ManagerStockController {
    @Autowired
    private ProductRepository productRepository;
    private static final int LOW_STOCK_LIMIT = 5;
    @Autowired
    private PurchaseEntryRepository purchaseRepo;
    @Autowired
    private SalesEntryRepository salesRepo;
    @GetMapping("/add-stock")
    public String addStockPage(HttpSession session, Model model) {
       if (session.getAttribute("managerUser") == null) {
            return "redirect:/manager-login";
        }
        model.addAttribute("products", productRepository.findAll());
        return "add-stock";
    }
    @PostMapping("/update-stock")
    public String updateStock(@RequestParam Long productId,
                              @RequestParam int addedQuantity,
                              HttpSession session) {
       if(session.getAttribute("managerUser") == null) {
            return "redirect:/manager-login";
        }
       Product product = productRepository.findById(productId).orElseThrow();
       product.setQuantity(product.getQuantity() + addedQuantity);
        productRepository.save(product);
        return "redirect:/manager/add-stock";
    }
    @GetMapping("/low-stock")
    public String lowStockPage(HttpSession session, Model model) {
     if (session.getAttribute("managerUser") == null) {
                return "redirect:/manager-login";
     }
     List<Product> lowStockProducts =productRepository.findByQuantityLessThanEqual(LOW_STOCK_LIMIT);
            model.addAttribute("lowStockProducts", lowStockProducts);
            model.addAttribute("limit", LOW_STOCK_LIMIT);
            return "low-stock";
     }
     @GetMapping("/monitor-stock")
        public String monitorStock(HttpSession session, Model model) {
             if (session.getAttribute("managerUser") == null) {
                return "redirect:/manager-login";
         }
         List<Product> products = productRepository.findAll();
           model.addAttribute("products", products);
           return "monitor-stock";
        }
      @GetMapping("/sales-entry")
        public String salesEntryPage(HttpSession session, Model model) {
             if (session.getAttribute("managerUser") == null) {
                return "redirect:/manager-login";
            }
            model.addAttribute("products", productRepository.findAll());
            return "sales-entry";
        }
      @PostMapping("/sales-entry/save")
      public String saveSalesEntry(@RequestParam Long productId,@RequestParam int quantity,@RequestParam double totalAmount,Model model) {
             Product product = productRepository.findById(productId).orElseThrow();
             if (quantity > product.getQuantity()) {
                model.addAttribute("error", "Not enough stock available");
                model.addAttribute("products", productRepository.findAll());
                return "sales-entry";
            }
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
            SalesEntry entry = new SalesEntry();
            entry.setProduct(product);
            entry.setQuantity(quantity);
            entry.setTotalAmount(totalAmount);
            entry.setDate(LocalDate.now());
            salesRepo.save(entry);
            return "redirect:/manager/sales-entry";
        }
        @GetMapping("/entries")
        public String entriesPage(HttpSession session) {
            if (session.getAttribute("managerUser") == null) {
                return "redirect:/manager-login";
            }
            return "manager-entries";
        }
       @GetMapping("/purchase-entry")
            public String purchaseEntryPage(HttpSession session, Model model) {
               if (session.getAttribute("managerUser") == null) {
                    return "redirect:/manager-login";
                }
               model.addAttribute("products", productRepository.findAll());
                return "purchase-entry";
            }
       @PostMapping("/purchase-entry/save")
            public String savePurchaseEntry(@RequestParam Long productId,@RequestParam int quantity,RedirectAttributes redirectAttributes) {
                Product product = productRepository.findById(productId).orElseThrow();
                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
                PurchaseEntry purchase = new PurchaseEntry();
                purchase.setProduct(product);
                purchase.setQuantity(quantity);
                purchase.setTotalCost(product.getPrice() * quantity); 
                purchase.setDate(LocalDate.now());
                purchaseRepo.save(purchase);
                redirectAttributes.addFlashAttribute( "success", "Purchase entry saved successfully!");
                 return "redirect:/manager/purchase-entry";
            }}
    
