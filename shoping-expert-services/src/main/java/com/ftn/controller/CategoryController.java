package com.ftn.controller;

import com.ftn.dto.CategoryDTO;
import com.ftn.dto.ShoppingListDTO;
import com.ftn.service.ICategoryService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Transactional
    @GetMapping
    public GenericResponse<List<CategoryDTO>> getAllCategories() {
        GenericResponse<List<CategoryDTO>> response = new GenericResponse<>();
        List<CategoryDTO> categoryDTOS = categoryService.readAll();
        response.success(categoryDTOS);
        return response;
    }
}
