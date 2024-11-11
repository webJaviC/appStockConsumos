package com.printshop.service;

import com.printshop.model.Material;
import com.printshop.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MaterialService {
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material createMaterial(Material material) {
        if (materialRepository.existsByCode(material.getCode())) {
            throw new RuntimeException("Material with this code already exists");
        }
        return materialRepository.save(material);
    }

    public Optional<Material> findByCode(String code) {
        return materialRepository.findByCode(code);
    }

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    public Material updateMaterial(Material material) {
        if (!materialRepository.existsById(material.getId())) {
            throw new RuntimeException("Material not found");
        }
        return materialRepository.save(material);
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
}