package com.ftn.controller;

import com.ftn.dto.CategoryItemDTO;
import com.ftn.service.ICategoryItemService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
@RestController
@RequestMapping("/api/categoryItems")
public class CategoryItemController {

    @Autowired
    private ICategoryItemService categoryItemService;

    @Transactional
    @GetMapping
    public GenericResponse<List<CategoryItemDTO>> getAllCategoryItems() {
        GenericResponse<List<CategoryItemDTO>> response = new GenericResponse<>();
        List<CategoryItemDTO> categoryItemDTOS = categoryItemService.readAll();
        response.success(categoryItemDTOS);
        return response;
    }

    @Transactional
    @GetMapping(value = "/{categoryId}")
    public GenericResponse<List<CategoryItemDTO>> getSpecificCategoryItems(@PathVariable("categoryId") long categoryId) {
        GenericResponse<List<CategoryItemDTO>> response = new GenericResponse<>();
        List<CategoryItemDTO> categoryItemDTOS = categoryItemService.readAllForCategory(categoryId);
        response.success(categoryItemDTOS);
        return response;
    }
}
