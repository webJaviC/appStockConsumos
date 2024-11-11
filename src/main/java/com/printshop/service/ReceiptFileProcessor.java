package com.printshop.service;

import com.printshop.model.Receipt;
import com.printshop.model.Material;
import com.printshop.model.Quality;
import com.printshop.model.Weight;
import com.printshop.repository.QualityRepository;
import com.printshop.repository.WeightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
public class ReceiptFileProcessor {
    private final ReceiptService receiptService;
    private final QualityRepository qualityRepository;
    private final WeightRepository weightRepository;

    public ReceiptFileProcessor(ReceiptService receiptService, 
                              QualityRepository qualityRepository,
                              WeightRepository weightRepository) {
        this.receiptService = receiptService;
        this.qualityRepository = qualityRepository;
        this.weightRepository = weightRepository;
    }

    @Transactional
    public Receipt processFile(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            if (line == null || line.length() < 200) { // Validate minimum length
                throw new IllegalArgumentException("Invalid file format");
            }

            // Extract receipt number and date from first line
            String receiptNumber = line.substring(116, 124);
            String dateStr = line.substring(124, 132);
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yy"));

            // Create receipt
            Receipt receipt = new Receipt();
            receipt.setReceiptNumber(receiptNumber);
            receipt.setDate(date);
            receipt.setSupplier("AUTO_IMPORT"); // Default supplier for file imports
            receipt.setMaterials(new HashSet<>());

            // Process all lines
            do {
                Material material = processMaterialLine(line);
                receipt.getMaterials().add(material);
                material.setReceipt(receipt);
            } while ((line = reader.readLine()) != null);

            return receiptService.createReceipt(receipt);
        }
    }

    private Material processMaterialLine(String line) {
        Material material = new Material();
        
        // Extract material data
        String code = line.substring(1, 11);
        double length = Double.parseDouble(line.substring(29, 32)) / 10.0; // Convert to cm
        double width = Double.parseDouble(line.substring(32, 35)) / 10.0; // Convert to cm
        double weightValue = Double.parseDouble(line.substring(132, 135));
        double netWeight = Double.parseDouble(line.substring(67, 70)) / 10.0;
        double grossWeight = Double.parseDouble(line.substring(76, 79)) / 10.0;
        String qualityCode = line.substring(85, 88);

        // Set material properties
        material.setCode(code);
        material.setLength(length);
        material.setWidth(width);
        material.setNetWeight(netWeight);
        material.setGrossWeight(grossWeight);

        // Get or create quality
        Quality quality = qualityRepository.findByName(qualityCode)
            .orElseGet(() -> {
                Quality newQuality = new Quality();
                newQuality.setName(qualityCode);
                return qualityRepository.save(newQuality);
            });
        material.setQuality(quality);

        // Get or create weight
        String weightName = String.format("%.0fg", weightValue);
        Weight weight = weightRepository.findByName(weightName)
            .orElseGet(() -> {
                Weight newWeight = new Weight();
                newWeight.setName(weightName);
                return weightRepository.save(newWeight);
            });
        material.setWeight(weight);

        return material;
    }
}