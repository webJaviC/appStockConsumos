package com.printshop.controller;

import com.printshop.model.Material;
import com.printshop.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/production/materials")
@PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION')")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public String listMaterials(Model model) {
        model.addAttribute("materials", materialService.findAll());
        return "production/materials/list";
    }

    @GetMapping("/create")
    public String createMaterialForm(Model model) {
        model.addAttribute("material", new Material());
        return "production/materials/form";
    }

    @PostMapping("/create")
    public String createMaterial(@ModelAttribute Material material) {
        materialService.createMaterial(material);
        return "redirect:/production/materials";
    }

    @GetMapping("/{id}/edit")
    public String editMaterialForm(@PathVariable Long id, Model model) {
        materialService.findByCode(id.toString()).ifPresent(material ->
            model.addAttribute("material", material));
        return "production/materials/form";
    }

    @PostMapping("/{id}/edit")
    public String updateMaterial(@PathVariable Long id, @ModelAttribute Material material) {
        material.setId(id);
        materialService.updateMaterial(material);
        return "redirect:/production/materials";
    }
}