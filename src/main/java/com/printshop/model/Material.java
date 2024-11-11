package com.printshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "quality_id", nullable = false)
    private Quality quality;

    @ManyToOne
    @JoinColumn(name = "weight_id", nullable = false)
    private Weight weight;

    private Double grossWeight;
    private Double netWeight;
    private Double width;
    private Double length;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}