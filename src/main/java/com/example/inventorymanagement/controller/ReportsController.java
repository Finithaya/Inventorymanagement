package com.example.inventorymanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.model.PurchaseEntry;
import com.example.inventorymanagement.model.SalesEntry;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.PurchaseEntryRepository;
import com.example.inventorymanagement.repository.SalesEntryRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("/reports")
public class ReportsController {
    @Autowired
    private SalesEntryRepository saleRepository;
    @Autowired
    private PurchaseEntryRepository purchaseRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public String reportsHome() {
        return "reports";
    }
    @GetMapping("/sales")
    public String salesReport(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Long productId,
            Model model) {
        List<SalesEntry> sales = saleRepository.findAll();
        if (productId != null) {
            sales = sales.stream()
                    .filter(s -> s.getProduct().getId().equals(productId))
                    .toList();
        }
       if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate from = LocalDate.parse(fromDate);
            sales = sales.stream()
                    .filter(s -> !s.getDate().isBefore(from))
                    .toList();
        }
      if (toDate != null && !toDate.isEmpty()) {
            LocalDate to = LocalDate.parse(toDate);
            sales = sales.stream()
                    .filter(s -> !s.getDate().isAfter(to))
                    .toList();
        }
        model.addAttribute("sales", sales);
        model.addAttribute("products", productRepository.findAll());
        long totalSales = sales.size();
        double totalRevenue = sales.stream().mapToDouble(SalesEntry::getTotalAmount).sum();
        int totalQuantity = sales.stream().mapToInt(SalesEntry::getQuantity).sum();
        Map<String, Integer> productSalesMap = new HashMap<>();
        for (SalesEntry sale : sales) {
            String productName = sale.getProduct().getName();
            productSalesMap.put(productName,
                    productSalesMap.getOrDefault(productName, 0) + sale.getQuantity());
        }
        String bestProduct = "N/A";
        if (!productSalesMap.isEmpty()) {
            bestProduct = Collections.max(productSalesMap.entrySet(),
                    Map.Entry.comparingByValue()).getKey();
        }
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("bestProduct", bestProduct);
        List<String> chartDates = sales.stream().map(s -> s.getDate().toString()).toList();
        List<Double> chartAmounts = sales.stream() .map(SalesEntry::getTotalAmount).toList();
        model.addAttribute("chartDates", chartDates);
        model.addAttribute("chartAmounts", chartAmounts);
        return "sales-report";
    }
    @GetMapping("/purchase")
    public String purchaseReport(Model model) {
        model.addAttribute("purchases", purchaseRepository.findAll());
        return "purchase-report";
    }
    @GetMapping("/low-stock")
    public String lowStockReport(Model model) {
       List<Product> lowStockProducts=productRepository.findByQuantityLessThanEqual(10);
       model.addAttribute("products", lowStockProducts);
       return "low-stock-report";
    }
    @GetMapping("/sales/excel")
    public void exportSalesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=sales-report.xlsx");
        List<SalesEntry> sales = saleRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Product");
        headerRow.createCell(2).setCellValue("Quantity");
        headerRow.createCell(3).setCellValue("Total Amount");
        headerRow.createCell(4).setCellValue("Date");
        int rowCount = 1;
        for (SalesEntry sale : sales) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(sale.getId());
            row.createCell(1).setCellValue(sale.getProduct() != null ? sale.getProduct().getName() : "N/A");
            row.createCell(2).setCellValue(sale.getQuantity());
            row.createCell(3).setCellValue(sale.getTotalAmount());
            row.createCell(4).setCellValue(sale.getDate().toString());
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/purchase/excel")
    public void exportPurchaseToExcel(HttpServletResponse response) throws IOException {
         response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
         response.setHeader("Content-Disposition", "attachment; filename=purchase-report.xlsx");
         List<PurchaseEntry> purchases = purchaseRepository.findAll();
         Workbook workbook = new XSSFWorkbook();
         Sheet sheet=workbook.createSheet("Purchase Report");
         Row headerRow = sheet.createRow(0);
         headerRow.createCell(0).setCellValue("ID");
         headerRow.createCell(1).setCellValue("Product");
         headerRow.createCell(2).setCellValue("Quantity");
         headerRow.createCell(3).setCellValue("Total Cost");
         headerRow.createCell(4).setCellValue("Date");
         int rowCount = 1;
         for (PurchaseEntry purchase : purchases) {Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(purchase.getId());
            row.createCell(1).setCellValue(purchase.getProduct() != null  ? purchase.getProduct().getName()  : "N/A");
            row.createCell(2).setCellValue(purchase.getQuantity());
            row.createCell(3).setCellValue(purchase.getTotalCost());
            row.createCell(4).setCellValue(purchase.getDate() != null ? purchase.getDate().toString() : "");
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "sales-report";
    }
}