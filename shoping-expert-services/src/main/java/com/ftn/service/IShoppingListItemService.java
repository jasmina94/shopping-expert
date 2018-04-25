package com.ftn.service;

/**
 * Created by milca on 4/25/2018.
 */
public interface IShoppingListItemService {

    int getNumberOfPurchasedItems(Long listId);

    int getNumberOfItems(Long listId);
}
