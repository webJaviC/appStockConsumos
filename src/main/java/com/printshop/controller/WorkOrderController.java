package com.printshop.controller;

import com.printshop.model.WorkOrder;
import com.printshop.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/production/work-orders")
@PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
public class WorkOrderController {
    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public String listWorkOrders(Model model) {
        model.addAttribute("workOrders", workOrderService.findAllActive());
        return "production/work-orders/list";
    }

    @GetMapping("/create")
    public String createWorkOrderForm(Model model) {
        model.addAttribute("workOrder", new WorkOrder());
        return "production/work-orders/form";
    }

    @PostMapping("/create")
    public String createWorkOrder(@ModelAttribute WorkOrder workOrder) {
        workOrderService.createWorkOrder(workOrder);
        return "redirect:/production/work-orders";
    }

    @GetMapping("/{id}")
    public String viewWorkOrder(@PathVariable Long id, Model model) {
        workOrderService.findById(id).ifPresent(workOrder -> 
            model.addAttribute("workOrder", workOrder));
        return "production/work-orders/view";
    }

    @PostMapping("/{id}/assign-material")
    public String assignMaterial(@PathVariable Long id, 
                               @RequestParam String materialCode,
                               @RequestParam Integer orderNumber) {
        workOrderService.assignMaterial(id, materialCode, orderNumber);
        return "redirect:/production/work-orders/" + id;
    }

    @PostMapping("/{id}/close")
    public String closeWorkOrder(@PathVariable Long id) {
        workOrderService.closeWorkOrder(id);
        return "redirect:/production/work-orders";
    }
}