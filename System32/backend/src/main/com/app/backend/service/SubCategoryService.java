
package com.app.backend.service;

import com.app.backend.model.SubCategory;
import com.app.backend.repository.SubCategoryRepository;
import com.app.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    private  SubCategoryRepository subCategoryRepository;

    @Autowired 
    public CategoryRepository categoryRepository;
    

    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public List<SubCategory> findByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    public SubCategory findById(Long id) {
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory create(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory update(Long id, SubCategory subCategoryDetails) {
        SubCategory subCategory = findById(id);
        subCategory.setName(subCategoryDetails.getName());
        subCategory.setDescription(subCategoryDetails.getDescription());
        subCategory.setCategory(subCategoryDetails.getCategory());
        return subCategoryRepository.save(subCategory);
    }

    public void delete(Long id) {
        SubCategory subCategory = findById(id);
        subCategoryRepository.delete(subCategory);
    }
}


        
