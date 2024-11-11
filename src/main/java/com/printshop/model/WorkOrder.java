package com.printshop.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "work_orders")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String routeNumber;

    private String customerNumber;
    private String productNumber;
    private String description;

    @ManyToOne
    @JoinColumn(name = "weight_id")
    private Weight weight;

    private Double requiredWeight;
    private Double width;
    private Double length;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status = WorkOrderStatus.OPEN;

    @OneToMany(mappedBy = "workOrder")
    private Set<MaterialAssignment> materialAssignments;
}

enum WorkOrderStatus {
    OPEN,
    CLOSED
}