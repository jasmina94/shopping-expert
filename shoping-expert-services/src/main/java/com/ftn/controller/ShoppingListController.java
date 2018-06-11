package com.ftn.controller;

import com.ftn.dto.ShoppingListDTO;
import com.ftn.service.IShoppingListService;
import com.ftn.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

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
    public GenericResponse<List<ShoppingListDTO>> getListsByStatus(@PathVariable Boolean isArchived, @PathVariable Long userId) {
        GenericResponse<List<ShoppingListDTO>> response = new GenericResponse<>();
        List<ShoppingListDTO> listShowDTOs = shoppingListService.getListsForUserByStatus(userId, isArchived);
        response.success(listShowDTOs);
        return response;
    }

    @Transactional
    @PostMapping("/uploadList/{userId}")
    public GenericResponse<Boolean> uploadList(@PathVariable Long userId, @RequestBody List<ShoppingListDTO> shoppingListDTOS) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.saveListsFromLocalStorage(userId, shoppingListDTOS);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error while uploading lists.");
        }
        return response;
    }

    @Transactional
    @PostMapping("/create/{listName}/{userId}")
    public GenericResponse<ShoppingListDTO> create(@PathVariable String listName, @PathVariable Long userId) {
        GenericResponse<ShoppingListDTO> response = new GenericResponse<>();
        ShoppingListDTO shoppingListDTO = shoppingListService.create(listName, userId);
        if(shoppingListDTO != null){
//            response.setSuccessfulOperation(true);
            response.success(shoppingListDTO);
        }else {
            response.error("Server side error while creating new list.");
        }
        return response;
    }

    @Transactional
    @PutMapping("/updateName/{listId}/{listName}")
    public GenericResponse rename(@PathVariable Long listId, @PathVariable String listName) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.updateName((long) listId, listName);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error while renaming list.");
        }
        return response;
    }

    @Transactional
    @DeleteMapping("/archive/{listId}")
    public GenericResponse<ShoppingListDTO> archive(@PathVariable Long listId) {
        GenericResponse<ShoppingListDTO> response = new GenericResponse<>();
        ShoppingListDTO shoppingListDTO = shoppingListService.archive(listId);
        if(shoppingListDTO != null){
            response.success(shoppingListDTO);
        }else {
            response.error("Server side error while archiving list.");
        }
        return response;
    }

    @Transactional
    @PutMapping("/restore/{listId}")
    public GenericResponse<Boolean> restore(@PathVariable Long listId) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.restore(listId);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error while activating list.");
        }
        return response;
    }

    @Transactional
    @PutMapping("/makeSecret/{listId}/{password}")
    public GenericResponse<Boolean> makeSecret(@PathVariable Long listId, @PathVariable @NotEmpty String password) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.makeSecret(listId, password);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error making list secret.");
        }
        return response;
    }

    @Transactional
    @PutMapping("/makePublic/{listId}")
    public GenericResponse<Boolean> makePublic(@PathVariable Long listId) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.makePublic(listId);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error making list public.");
        }
        return response;
    }

    @Transactional
    @PutMapping("/addReminder/{listId}")
    public GenericResponse<Boolean> addReminder(@PathVariable("listId") Long listId, @RequestBody Map<String, String> timeAndDate) {
        GenericResponse response = new GenericResponse();
        boolean success = shoppingListService.addReminder(listId, timeAndDate.get("date"), timeAndDate.get("time"));
        if(success) {
            response.success(true);
        } else {
            response.success(false);
        }
        return response;
    }

    @Transactional
    @PutMapping("/removeReminder/{listId}")
    public GenericResponse<Boolean> removeReminder(@PathVariable("listId") Long listId) {
        GenericResponse response = new GenericResponse();
        boolean success = shoppingListService.removeReminder(listId);
        if(success) {
            response.success(true);
        } else {
            response.success(false);
        }
        return response;
    }

    @Transactional
    @PutMapping("/shareList/{listId}/{sharedWith}")
    public GenericResponse<List<String>> shareList(@PathVariable Long listId, @PathVariable String sharedWith) {
        GenericResponse response = new GenericResponse();
        List<String> shareDevices = shoppingListService.shareList(listId, sharedWith);
        if(shareDevices == null) {
            response.success(false);
        } else {
            response.success(true);
            response.setEntity(shareDevices);
        }
        return response;
    }

    @Transactional
    @PutMapping("/unShareList/{listId}/{sharedWith}")
    public GenericResponse<Boolean> unShareList(@PathVariable Long listId, @PathVariable String sharedWith) {
        GenericResponse response = new GenericResponse();
        boolean isSuccess = shoppingListService.unShareList(listId, sharedWith);
        if(!isSuccess) {
            response.success(false);
        } else {
            response.success(true);
        }
        return response;
    }
    
    @Transactional
    @PutMapping("/updateLocation/{listId}/{latitude}/{longitude}")
    public GenericResponse<Boolean> updateLocation(@PathVariable Long listId, @PathVariable Double latitude, @PathVariable Double longitude) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.updateLocation(listId, latitude, longitude);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error while activating list.");
        }
        return response;
    }
    
    @Transactional
    @DeleteMapping("/deleteList/{listId}")
    public GenericResponse<Boolean> deleteList(@PathVariable Long listId) {
        GenericResponse<Boolean> response = new GenericResponse<>();
        boolean result = shoppingListService.deleteList(listId);
        if(result){
            response.success(true);
        }else {
            response.error("Server side error while activating list.");
        }
        return response;
    }
    
    @Transactional
    @GetMapping("/getFriendList/{listId}/{userId}")
    public GenericResponse<Map<String, Boolean>> getFriendList(@PathVariable("listId") Long listId, @PathVariable("userId") Long userId) {
        GenericResponse response = new GenericResponse();
        Map<String, Boolean> friendList = shoppingListService.getFriendList(listId, userId);
        if(friendList == null) {
            response.success(false);
        } else {
            response.success(true);
            response.setEntity(friendList);
        }
        return response;
    }

    @Transactional
    @GetMapping("/getDevicesThatNeedReminder/{listId}")
    public GenericResponse<List<String>> getDevicesThatNeedReminder(@PathVariable("listId") Long listId) {
        GenericResponse response = new GenericResponse();
        List<String> list = shoppingListService.getListForReminder(listId);

        if(list == null) {
            response.success(false);
        } else {
            response.setEntity(list);
            response.success(true);
        }
        return response;
    }
}
