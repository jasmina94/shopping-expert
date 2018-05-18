package com.ftn.controller;

import com.ftn.service.IShoppingListService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Created by milca on 4/25/2018.
 */
@RestController
@RequestMapping("/api/lists")
public class ShoppingListController {

    @Autowired
    private IShoppingListService iShoppingListService;

    @Transactional
    @GetMapping("/listsByStatus/{isArchived}")
    public ResponseEntity getListsByStatus(@PathVariable Boolean isArchived) {

        return new ResponseEntity<>(iShoppingListService.getListsByArchivedStatus((long) 1, isArchived), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/create/{listName}")
    public GenericResponse Create(@PathVariable String listName) {
        iShoppingListService.create(listName, (long) 1);
        GenericResponse<String> response = new GenericResponse<>();

        return response;
    }

    @Transactional
    @PostMapping("/updateName/{listId}/{listName}")
    public ResponseEntity UpdateName(@PathVariable Long listId, @PathVariable String listName) {
        iShoppingListService.updateName((long) 1, listName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/archive/{listId}")
    public ResponseEntity Archive(@PathVariable Long listId) {

        iShoppingListService.archive((long) 1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/revive/{listId}")
    public ResponseEntity Revive(@PathVariable Long listId) {

        iShoppingListService.revive((long) 1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/makeSecret/{listId}")
    public ResponseEntity MakeSecret(@PathVariable Long listId, @RequestBody @NotEmpty String password) {
        iShoppingListService.makeSecret(listId, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/makePublic/{listId}")
    public ResponseEntity MakePublic(@PathVariable Long listId, @RequestBody @NotEmpty String password) {
        if (!iShoppingListService.MakePublic(listId, password))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/addReminder/{listId}/{reminder}")
    public ResponseEntity AddReminder(@PathVariable Long listId, @PathVariable LocalDateTime reminder) {
        iShoppingListService.addReminder(listId, reminder);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("shareList/{listId}/{sharedWith}")
    public ResponseEntity shareList(@PathVariable Long listId, @PathVariable String sharedWith) {
        iShoppingListService.shareList(listId, sharedWith);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
