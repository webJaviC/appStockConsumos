package com.printshop.service;

import com.printshop.model.WorkOrder;
import com.printshop.model.WorkOrderStatus;
import com.printshop.model.Material;
import com.printshop.model.MaterialAssignment;
import com.printshop.repository.WorkOrderRepository;
import com.printshop.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final MaterialRepository materialRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, MaterialRepository materialRepository) {
        this.workOrderRepository = workOrderRepository;
        this.materialRepository = materialRepository;
    }

    public WorkOrder createWorkOrder(WorkOrder workOrder) {
        workOrder.setDate(LocalDate.now());
        workOrder.setStatus(WorkOrderStatus.OPEN);
        return workOrderRepository.save(workOrder);
    }

    public Optional<WorkOrder> findById(Long id) {
        return workOrderRepository.findById(id);
    }

    public List<WorkOrder> findAllActive() {
        return workOrderRepository.findByStatus(WorkOrderStatus.OPEN);
    }

    public MaterialAssignment assignMaterial(Long workOrderId, String materialCode, Integer orderNumber) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
            .orElseThrow(() -> new RuntimeException("Work order not found"));
        
        Material material = materialRepository.findByCode(materialCode)
            .orElseThrow(() -> new RuntimeException("Material not found"));

        MaterialAssignment assignment = new MaterialAssignment();
        assignment.setWorkOrder(workOrder);
        assignment.setMaterial(material);
        assignment.setOrderNumber(orderNumber);
        assignment.setUpdatedNetWeight(material.getNetWeight());

        workOrder.getMaterialAssignments().add(assignment);
        workOrderRepository.save(workOrder);

        return assignment;
    }

    public void closeWorkOrder(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Work order not found"));
        
        workOrder.setStatus(WorkOrderStatus.CLOSED);
        workOrderRepository.save(workOrder);
    }
}