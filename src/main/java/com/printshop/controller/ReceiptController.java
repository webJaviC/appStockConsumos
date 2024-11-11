package com.printshop.controller;

import com.printshop.model.Receipt;
import com.printshop.service.ReceiptService;
import com.printshop.service.ReceiptFileProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/production/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;
    private final ReceiptFileProcessor receiptFileProcessor;

    public ReceiptController(ReceiptService receiptService, ReceiptFileProcessor receiptFileProcessor) {
        this.receiptService = receiptService;
        this.receiptFileProcessor = receiptFileProcessor;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
    public String listReceipts(Model model) {
        model.addAttribute("receipts", receiptService.findAll());
        return "production/receipts/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
    public String viewReceipt(@PathVariable Long id, Model model) {
        receiptService.findById(id).ifPresent(receipt -> 
            model.addAttribute("receipt", receipt));
        return "production/receipts/view";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
    public String createReceiptForm(Model model) {
        model.addAttribute("receipt", new Receipt());
        return "production/receipts/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
    public String createReceipt(@ModelAttribute Receipt receipt) {
        receiptService.createReceipt(receipt);
        return "redirect:/production/receipts";
    }

    @GetMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String showImportForm() {
        return "production/receipts/import";
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String importReceiptFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Receipt receipt = receiptFileProcessor.processFile(file.getInputStream());
            return "redirect:/production/receipts/" + receipt.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            return "production/receipts/import";
        }
    }
}