
package com.app.backend.service;

import com.app.backend.model.Subcategory;
import com.app.backend.repository.SubCategoryRepository;
import com.app.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired 
    public CategoryRepository categoryRepository;
    

    public List<Subcategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public List<Subcategory> findByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    public Subcategory findById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Subcategoria no encontrada"));
    }

    public Subcategory create(Subcategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public Subcategory update(Long id, Subcategory subCategoryDetails) {
        Subcategory subCategory = findById(id);
        subCategory.setName(subCategoryDetails.getName());
        subCategory.setDescription(subCategoryDetails.getDescription());
        subCategory.setCategory(subCategoryDetails.getCategory());
        return subCategoryRepository.save(subCategory);
    }

    public void delete(Long id) {
        Subcategory subCategory = findById(id);
        subCategoryRepository.delete(subCategory);
    }
}

        
