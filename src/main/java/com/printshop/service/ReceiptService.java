package com.printshop.service;

import com.printshop.model.Receipt;
import com.printshop.repository.ReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt createReceipt(Receipt receipt) {
        if (receiptRepository.existsByReceiptNumber(receipt.getReceiptNumber())) {
            throw new RuntimeException("Receipt with this number already exists");
        }
        receipt.setDate(LocalDate.now());
        return receiptRepository.save(receipt);
    }

    public Optional<Receipt> findByReceiptNumber(String receiptNumber) {
        return receiptRepository.findByReceiptNumber(receiptNumber);
    }

    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    public Receipt updateReceipt(Receipt receipt) {
        if (!receiptRepository.existsById(receipt.getId())) {
            throw new RuntimeException("Receipt not found");
        }
        return receiptRepository.save(receipt);
    }
}