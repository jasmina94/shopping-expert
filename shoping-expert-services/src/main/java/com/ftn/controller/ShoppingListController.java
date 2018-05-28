package com.ftn.controller;

import com.ftn.dto.ShoppingListShowDto;
import com.ftn.service.IShoppingListService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {

    @Autowired
    private IShoppingListService shoppingListService;

    @Transactional
    @GetMapping("/listsByStatus/{isArchived}/{userId}")
    public GenericResponse<List<ShoppingListShowDto>> getListsByStatus(@PathVariable Boolean isArchived, @PathVariable Long userId) {
        GenericResponse<List<ShoppingListShowDto>> response = new GenericResponse<>();
        response.setEntity(shoppingListService.getListsByArchivedStatus(userId, isArchived));
        return response;
    }

    @Transactional
    @PostMapping("/create/{listName}/{userId}")
    public GenericResponse Create(@PathVariable String listName, @PathVariable Long userId) {
        shoppingListService.create(listName, userId);
        return new GenericResponse<>();
    }

    @Transactional
    @PutMapping("/updateName/{listId}/{listName}")
    public GenericResponse UpdateName(@PathVariable Long listId, @PathVariable String listName) {
        shoppingListService.updateName((long) listId, listName);
        return new GenericResponse<>();
    }

    @Transactional
    @DeleteMapping("/archive/{listId}")
    public GenericResponse archive(@PathVariable Long listId) {
        shoppingListService.archive(listId);
        return new GenericResponse();
    }

    @Transactional
    @DeleteMapping("/revive/{listId}")
    public GenericResponse Revive(@PathVariable Long listId) {
        shoppingListService.revive(listId);
        return new GenericResponse();
    }

    @Transactional
    @PutMapping("/makeSecret/{listId}/{password}")
    public GenericResponse MakeSecret(@PathVariable Long listId, @PathVariable @NotEmpty String password) {
        shoppingListService.makeSecret(listId, password);
        return new GenericResponse();
    }

    @Transactional
    @PutMapping("/makePublic/{listId}/{password}")
    public GenericResponse<Boolean> MakePublic(@PathVariable Long listId, @PathVariable @NotEmpty String password) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setEntity(shoppingListService.MakePublic(listId, password));
        return genericResponse;
    }

    @Transactional
    @PutMapping("/addReminder/{listId}/{reminder}")
    public ResponseEntity AddReminder(@PathVariable Long listId, @PathVariable LocalDateTime reminder) {
        shoppingListService.addReminder(listId, reminder);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("shareList/{listId}/{sharedWith}")
    public ResponseEntity shareList(@PathVariable Long listId, @PathVariable String sharedWith) {
        shoppingListService.shareList(listId, sharedWith);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
