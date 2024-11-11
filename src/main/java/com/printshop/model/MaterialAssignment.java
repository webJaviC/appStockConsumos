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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Double getUpdatedNetWeight() {
		return updatedNetWeight;
	}
	public void setUpdatedNetWeight(Double updatedNetWeight) {
		this.updatedNetWeight = updatedNetWeight;
	}
    
    
}