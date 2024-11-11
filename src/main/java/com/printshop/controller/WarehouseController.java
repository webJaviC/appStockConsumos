package com.printshop.controller;

import com.printshop.dto.MaterialScanResponse;
import com.printshop.model.MaterialAssignment;
import com.printshop.model.WorkOrder;
import com.printshop.service.MaterialService;
import com.printshop.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/warehouse")
@PreAuthorize("hasRole('WAREHOUSE')")
public class WarehouseController {
    private final WorkOrderService workOrderService;
    private final MaterialService materialService;

    public WarehouseController(WorkOrderService workOrderService, MaterialService materialService) {
        this.workOrderService = workOrderService;
        this.materialService = materialService;
    }

    @GetMapping("/work-orders")
    public String listWorkOrders(Model model) {
        model.addAttribute("workOrders", workOrderService.findAllActive());
        return "warehouse/work-orders";
    }

    @GetMapping("/work-orders/{id}")
    public String viewWorkOrder(@PathVariable Long id, Model model) {
        WorkOrder workOrder = workOrderService.findById(id)
            .orElseThrow(() -> new RuntimeException("Work order not found"));
        model.addAttribute("workOrder", workOrder);
        return "warehouse/work-order-detail";
    }

    @PostMapping("/work-orders/{id}/assign-material")
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

    @GetMapping("/inventory")
    public String viewInventory(Model model) {
        model.addAttribute("materials", materialService.findAll());
        return "warehouse/inventory";
    }

    @PostMapping("/api/scan-material")
    @ResponseBody
    public ResponseEntity<MaterialScanResponse> scanMaterial(@RequestParam String code) {
        return materialService.findByCode(code)
            .map(material -> ResponseEntity.ok(new MaterialScanResponse(true, "Material found", material)))
            .orElse(ResponseEntity.ok(new MaterialScanResponse(false, "Material not found", null)));
    }
}