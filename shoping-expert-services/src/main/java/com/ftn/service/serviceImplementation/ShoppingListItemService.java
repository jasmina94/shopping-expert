package com.ftn.service.serviceImplementation;

import com.ftn.dto.CategoryItemDTO;
import com.ftn.dto.ShoppingListItemDTO;
import com.ftn.entity.ShoppingListItem;
import com.ftn.repository.ShoppingListItemRepository;
import com.ftn.service.IShoppingListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by milca on 4/25/2018.
 */
@Service
public class ShoppingListItemService implements IShoppingListItemService {

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;

    @Override
    public int getNumberOfPurchasedItems(Long listId) {
        List<ShoppingListItem> list = shoppingListItemRepository.findByIsPurchasedAndShoppingListId(true, listId);
        return list.size();
    }

    @Override
    public int getNumberOfItems(Long listId) {
        List<ShoppingListItem> itemsForList = shoppingListItemRepository.findByShoppingListId(listId);
        return itemsForList.size();
    }

    @Override
    public boolean addItemToList(ShoppingListItemDTO shoppingListItemDTO, long listId) {
        boolean success = true;
        shoppingListItemDTO.setShoppingListId(listId);
        ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemDTO);
        try {
            shoppingListItem = shoppingListItemRepository.save(shoppingListItem);
        }catch (Exception e){
            success = false;
        }
        return success;
    }

    @Override
    public List<ShoppingListItemDTO> readFromList(long listId) {
        return shoppingListItemRepository.findByShoppingListId(listId).stream()
                .map(shoppingListItem -> new ShoppingListItemDTO(shoppingListItem)).collect(Collectors.toList());

    }

    @Override
    public boolean addItemToList(CategoryItemDTO categoryItemDTO, long listId) {
        boolean success = true;
        ShoppingListItemDTO shoppingListItemDTO = new ShoppingListItemDTO(categoryItemDTO, listId);
        ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListItemDTO);
        try {
            shoppingListItem = shoppingListItemRepository.save(shoppingListItem);
        }catch (Exception e){
            success = false;
        }
        return success;
    }

    @Override
    public boolean buyItem(long itemId) {
        boolean success = true;
        try {
            ShoppingListItem shoppingListItem = shoppingListItemRepository.findById(itemId).orElseThrow(NullPointerException::new);
            shoppingListItem.setIsPurchased(true);
            shoppingListItemRepository.save(shoppingListItem);
        }catch (NullPointerException e){
            success = false;
        }
        return success;
    }
}
