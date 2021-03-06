package com.ftn.service;

import com.ftn.dto.ShoppingListDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by milca on 4/25/2018.
 */
public interface IShoppingListService {

	List<ShoppingListDTO> getListsForUserByStatus(Long loggedUserId, boolean isArchived);

    ShoppingListDTO create(String listName, Long loggedUserId);

    ShoppingListDTO archive(long listId);

    boolean makeSecret(Long listId, String password);

    boolean makePublic(Long listId);

    boolean restore(long listId);

    boolean updateName(long listId, String listName);

    List<String> shareList(Long listId, String sharedWith);

    boolean saveListsFromLocalStorage(Long loggedUserId, List<ShoppingListDTO> list);
    
    boolean updateLocation(long listId, Double latitude, Double longitude);
    
    boolean deleteList(long listId);

    Map<String, Boolean> getFriendList(Long listId, Long userId);

    boolean unShareList(Long listId, String unShareEmail);

    boolean removeReminder(Long listId);

    boolean addReminder(Long listId, String date, String time);
}
