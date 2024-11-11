package com.printshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "material_assignments")
public class MaterialAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    private Integer orderNumber;
    private Double updatedNetWeight;
}