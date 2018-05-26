package com.ftn.service.serviceImplementation;

import com.ftn.dto.ShoppingListShowDto;
import com.ftn.entity.ShoppingList;
import com.ftn.repository.ShoppingListRepository;
import com.ftn.service.IShoppingListItemService;
import com.ftn.service.IShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by milca on 4/25/2018.
 */
@Service
public class ShoppingListService implements IShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private IShoppingListItemService iShoppingListItemService;

    @Autowired
    private EmailService emailService;

    @Override
    public List<ShoppingListShowDto> getListsByArchivedStatus(Long loggedUserId, boolean isArchived) {
        List<ShoppingList> list = shoppingListRepository.findByCreatorIdAndIsArchived(loggedUserId, isArchived);

        List<ShoppingListShowDto> listDto = list.stream().map(l -> {
            ShoppingListShowDto dto = new ShoppingListShowDto(l);
            dto.setBoughtItems(iShoppingListItemService.getNumberOfPurchasedItems(l.getId()));
            dto.setNumberOfItems(iShoppingListItemService.getNumberOfItems(l.getId()));

            return dto;
        }).collect(Collectors.toList());


        return listDto;
    }

    @Override
    public void create(String listName, Long loggedUserId) {
        ShoppingList shoppingList = new ShoppingList();

        shoppingList.setListName(listName);
        shoppingList.setCreatorId(loggedUserId);

        shoppingListRepository.save(shoppingList);
    }

    @Override
    public void archive(long listId) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        shoppingList.setIsArchived(true);
        shoppingListRepository.save(shoppingList);
    }

    @Override
    public void makeSecret(Long listId, String password) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);

        shoppingList.setIsSecret(true);
        shoppingList.setAccessPassword(password);

        shoppingListRepository.save(shoppingList);
    }

    @Override
    public boolean MakePublic(Long listId, String password) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);

        if(!shoppingList.getAccessPassword().equals(password))
            return false;
        else {
            shoppingList.setIsSecret(false);
            shoppingList.setAccessPassword("");
            return true;
        }
    }

    @Override
    public void addReminder(Long listId, LocalDateTime reminder) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        shoppingList.setReminder(reminder);
        shoppingListRepository.save(shoppingList);
    }

    @Override
    public void revive(long listId) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        shoppingList.setIsArchived(false);
        shoppingListRepository.save(shoppingList);
    }

    @Override
    public void updateName(long listId, String listName) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        shoppingList.setListName(listName);
        shoppingListRepository.save(shoppingList);
    }

    @Override
    public void shareList(Long listId, String sharedWith) {
        //check if user is registratet if not, send email invitation
        String curentlyLoggedUserName = "Milica";
        String subject = "MDJ - List shared";
        // will add url to
        String message ="Mr/s " + curentlyLoggedUserName + ", \n " + curentlyLoggedUserName + " has just shared shopping list with you. Click on notification. \n ";
        emailService.sendEmail(subject, message, sharedWith);
    }

}
