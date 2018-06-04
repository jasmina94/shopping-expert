package com.ftn.service;

import com.ftn.dto.ShoppingListDTO;
import com.ftn.entity.ShoppingList;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
public interface IShoppingListService {

	List<ShoppingListDTO> getListsForUserByStatus(Long loggedUserId, boolean isArchived);

    ShoppingListDTO create(String listName, Long loggedUserId);

    ShoppingListDTO archive(long listId);

    boolean makeSecret(Long listId, String password);

    boolean makePublic(Long listId);

    void addReminder(Long listId, LocalDateTime reminder);

    boolean restore(long listId);

    boolean updateName(long listId, String listName);

    boolean shareList(Long listId, String sharedWith);

    boolean saveListsFromLocalStorage(Long loggedUserId, List<ShoppingListDTO> list);
    
    boolean updateLocation(long listId, Double latitude, Double longitude);
    
    boolean deleteList(long listId);
}
