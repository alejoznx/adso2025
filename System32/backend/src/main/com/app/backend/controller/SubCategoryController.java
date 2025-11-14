package com.app.backend.controller;

import com.app.backend.model.Subcategory;
import com.app.backend.service.SubCategoryService;
import com.app.backend.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin(origins = "*")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<List<Subcategory>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.findAll());
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<List<Subcategory>> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subCategoryService.findByCategoryId(categoryId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<Subcategory> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<Subcategory> createSubCategory(@RequestBody Subcategory subcategory) {
        return ResponseEntity.ok(subCategoryService.create(subcategory));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN' , 'COORDINADOR')")
    public ResponseEntity<Subcategory> updateSubCategory(@PathVariable Long id, @RequestBody Subcategory subcategory) {
        return ResponseEntity.ok(subCategoryService.update(id, subcategory));
    }
    

    @DeleteMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteSubCategory(@PathVariable Long id) {
        subCategoryService.delete(id);
        return ResponseEntity.ok(new MessageResponse("SubCategoria eliminada con exito"));
    }
}