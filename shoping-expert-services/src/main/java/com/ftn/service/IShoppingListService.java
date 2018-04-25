package com.ftn.service;

import com.ftn.dto.ShoppingListShowDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
public interface IShoppingListService {

    List<ShoppingListShowDto> getListsByArchivedStatus(Long loggedUserId, boolean isArchived);

    void create(String listName, Long loggedUserId);

    void archive(long listId);

    void makeSecret(Long listId, String password);

    boolean MakePublic(Long listId, String password);

    void addReminder(Long listId, LocalDateTime reminder);

    void revive(long listId);

    void updateName(long listId, String listName);
}
