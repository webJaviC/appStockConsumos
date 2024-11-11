package com.printshop.controller;

import com.printshop.model.MaterialAssignment;
import com.printshop.model.WorkOrder;
import com.printshop.service.MaterialService;
import com.printshop.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/warehouse")
@PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
public class WarehouseController {
    private final WorkOrderService workOrderService;
    private final MaterialService materialService;

    public WarehouseController(WorkOrderService workOrderService, MaterialService materialService) {
        this.workOrderService = workOrderService;
        this.materialService = materialService;
    }

    @GetMapping("/inventory")
    public String viewInventory(Model model) {
        model.addAttribute("materials", materialService.findAll());
        return "warehouse/inventory";
    }

    @GetMapping("/work-orders")
    public String listWorkOrders(Model model) {
        model.addAttribute("workOrders", workOrderService.findAllActive());
        return "warehouse/work-orders";
    }

    @GetMapping("/work-orders/{id}")
    public String viewWorkOrder(@PathVariable Long id, Model model) {
        workOrderService.findById(id).ifPresent(workOrder -> {
            model.addAttribute("workOrder", workOrder);
            model.addAttribute("materials", materialService.findAll());
        });
        return "warehouse/work-order-detail";
    }

    @PostMapping("/work-orders/{id}/assign")
    public String assignMaterial(@PathVariable Long id,
                               @RequestParam String materialCode,
                               @RequestParam Integer orderNumber,
                               @RequestParam(required = false) Double updatedNetWeight) {
        MaterialAssignment assignment = workOrderService.assignMaterial(id, materialCode, orderNumber);
        if (updatedNetWeight != null) {
            assignment.setUpdatedNetWeight(updatedNetWeight);
        }
        return "redirect:/warehouse/work-orders/" + id;
    }

    @PostMapping("/work-orders/{id}/close")
    public String closeWorkOrder(@PathVariable Long id) {
        workOrderService.closeWorkOrder(id);
        return "redirect:/warehouse/work-orders";
    }
}