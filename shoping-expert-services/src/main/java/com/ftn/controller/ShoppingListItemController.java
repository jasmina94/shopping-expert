package com.ftn.controller;

import com.ftn.dto.CategoryItemDTO;
import com.ftn.dto.ShoppingListItemDTO;
import com.ftn.service.IShoppingListItemService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jasmina on 11/06/2018.
 */
@RestController
@RequestMapping("/api/listItems")
public class ShoppingListItemController {

    @Autowired
    private IShoppingListItemService shoppingListItemService;

    @Transactional
    @GetMapping(value = "/{listId}")
    public GenericResponse<List<ShoppingListItemDTO>> getItemsForList(@PathVariable("listId") long listId) {
        GenericResponse<List<ShoppingListItemDTO>> response = new GenericResponse<>();
        List<ShoppingListItemDTO> shoppingListItemDTOS = shoppingListItemService.readFromList(listId);
        response.success(shoppingListItemDTOS);
        return response;
    }

    @Transactional
    @PostMapping(value = "/{listId}")
    public GenericResponse<Boolean> addItemToList(@PathVariable("listId") long listId, ShoppingListItemDTO shoppingListItemDTO) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean success = shoppingListItemService.addItemToList(shoppingListItemDTO, listId);
        if(success){
            response.success(true);
        }else {
            response.error("Server side error while adding item to list!");
        }
        return response;
    }
    
    @Transactional
    @PostMapping
    public GenericResponse<Boolean> updateItem(@RequestBody ShoppingListItemDTO shoppingListItemDTO) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean success = shoppingListItemService.updateItem(shoppingListItemDTO);
        if(success){
            response.success(true);
        }else {
            response.error("Server side error while updating item!");
        }
        return response;
    }
}
