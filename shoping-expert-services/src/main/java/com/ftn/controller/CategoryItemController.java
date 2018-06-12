package com.ftn.controller;

import com.ftn.dto.CategoryItemDTO;
import com.ftn.dto.ShoppingListItemDTO;
import com.ftn.service.ICategoryItemService;
import com.ftn.service.IShoppingListItemService;
import com.ftn.service.IShoppingListService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
@RestController
@RequestMapping("/api/categoryItems")
public class CategoryItemController {

    @Autowired
    private ICategoryItemService categoryItemService;

    @Autowired
    private IShoppingListItemService shoppingListItemService;

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

    @Transactional
    @GetMapping(value="/map")
    public GenericResponse<HashMap<String,  List<CategoryItemDTO>>> getMap(){
        GenericResponse<HashMap<String, List<CategoryItemDTO>>> response = new GenericResponse<>();
        HashMap<String,  List<CategoryItemDTO>> map = categoryItemService.readAllAsMap();
        response.success(map);
        return response;
    }


    @Transactional
    @PostMapping(value = "/{lisToAdd}")
    public GenericResponse<Boolean> createNewOtherItem(@RequestBody CategoryItemDTO categoryItemDTO,
                                                       @PathVariable("lisToAdd") long listToAddId){
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean success = categoryItemService.createCategoryAndShoppingItem(categoryItemDTO, listToAddId);
        if(success){
            response.success(true);
        }else {
            response.error("Server side error while creating new category item/shopping list item");
        }
        return response;
    }

    @Transactional
    @PostMapping(value = "/{categoryItemId}/{listToAdd}")
    public GenericResponse<Boolean> createNewOtherItem(@PathVariable("categoryItemId") long categoryItemId,
                                                       @PathVariable("listToAdd") long listId){
        GenericResponse<Boolean> response = new GenericResponse<>();
        CategoryItemDTO categoryItemDTO = categoryItemService.findById(categoryItemId);
        boolean success = shoppingListItemService.addItemToList(categoryItemDTO, listId);
        if(success){
            response.success(true);
        }else {
            response.error("Server side error");
        }
        return response;
    }
}
