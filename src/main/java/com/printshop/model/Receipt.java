package com.printshop.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String receiptNumber;

    private LocalDate date;

    @Column(nullable = false)
    private String supplier;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private Set<Material> materials;
}