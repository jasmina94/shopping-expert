package com.ftn.service;

import com.ftn.dto.ShoppingListItemDTO;

import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
public interface IShoppingListItemService {

    int getNumberOfPurchasedItems(Long listId);

    int getNumberOfItems(Long listId);

    boolean addItemToList(ShoppingListItemDTO shoppingListItemDTO, long listId);

    List<ShoppingListItemDTO> readFromList(long listId);

    boolean createListItem(ShoppingListItemDTO shoppingListItemDTO);
}
