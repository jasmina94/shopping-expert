package com.ftn.service.serviceImplementation;

import com.ftn.entity.ShoppingListItem;
import com.ftn.repository.ShoppingListItemRepository;
import com.ftn.service.IShoppingListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by milca on 4/25/2018.
 */
@Service
public class ShoppingListItemService implements IShoppingListItemService {

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Override
    public int getNumberOfPurchasedItems(Long listId) {

        List<ShoppingListItem> list = shoppingListItemRepository.findByIsPurchased(true);


        return (int) list.stream().filter(l -> l.getPartOfShoppingLists().contains(listId)).count();
    }

    @Override
    public int getNumberOfItems(Long listId) {
        return (int) shoppingListItemRepository.findAll().stream().filter(l -> l.getPartOfShoppingLists().contains(listId)).count();
    }
}
