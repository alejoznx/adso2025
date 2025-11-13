package com.app.backend.controller;

import com.app.backend.model.Category;
import com.app.backend.service.CategoryService;
import com.app.backend.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.Http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<List<SubCategory>> getAllSubCategories() {
        return ResponseEntity.ok(subcategoryService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<SubCategory> getsubCategoryById
    (@PathVariable Long id) {
        return ResponseEntity.ok(subcategoryService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<SubCategory> createSubCategory(@RequestBody SubCategory subcategory) {
        return ResponseEntity.ok(subcategoryService.create(subcategory));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN' , 'COORDINADOR')")    
    public ResponseEntity<SubCategory> updateSubCategory
    (@PathVariable Long id, @RequestBody SubCategory subcategory) {
        return ResponseEntity.ok(subcategoryService.update(id, subcategory));
    }

    @DeleteMapping(value ="/{id}"), produces = MediaType.APPLICATION_JSON_VALUE
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity.ok<SubCategory> deleteSubCategory(@PathVariable Long id, @RequestBody SubCategory subcategory) {
        subcategoryService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Categoría eliminada exitosamente"));
  
    }

}