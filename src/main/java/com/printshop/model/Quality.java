package com.printshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "qualities")
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}