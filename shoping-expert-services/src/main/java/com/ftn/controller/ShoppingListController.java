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
    @GetMapping("/listsByStatus/{isArchived}")
    public GenericResponse<List<ShoppingListShowDto>> getListsByStatus(@PathVariable Boolean isArchived) {
        GenericResponse<List<ShoppingListShowDto>> response = new GenericResponse<>();
        response.setEntity(shoppingListService.getListsByArchivedStatus((long) 1, isArchived));
        return response;
    }

    @Transactional
    @PostMapping("/create/{listName}")
    public GenericResponse Create(@PathVariable String listName) {
        shoppingListService.create(listName, (long) 1);
        GenericResponse<String> response = new GenericResponse<>();

        return response;
    }

    @Transactional
    @PostMapping("/updateName/{listId}/{listName}")
    public GenericResponse UpdateName(@PathVariable Long listId, @PathVariable String listName) {
        shoppingListService.updateName((long) 1, listName);
        return new GenericResponse<>();
    }

    @Transactional
    @DeleteMapping("/archive/{listId}")
    public GenericResponse archive(@PathVariable Long listId) {
        shoppingListService.archive((long) listId);
        return new GenericResponse();
    }

    @Transactional
    @DeleteMapping("/revive/{listId}")
    public ResponseEntity Revive(@PathVariable Long listId) {

        shoppingListService.revive((long) 1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/makeSecret/{listId}")
    public ResponseEntity MakeSecret(@PathVariable Long listId, @RequestBody @NotEmpty String password) {
        shoppingListService.makeSecret(listId, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/makePublic/{listId}")
    public ResponseEntity MakePublic(@PathVariable Long listId, @RequestBody @NotEmpty String password) {
        if (!shoppingListService.MakePublic(listId, password))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(HttpStatus.OK);
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
