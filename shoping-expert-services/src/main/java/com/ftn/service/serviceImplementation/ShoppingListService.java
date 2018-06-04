package com.ftn.service.serviceImplementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.dto.ShoppingListDTO;
import com.ftn.entity.ShoppingList;
import com.ftn.repository.ShoppingListRepository;
import com.ftn.service.IShoppingListItemService;
import com.ftn.service.IShoppingListService;

/**
 * Created by milca on 4/25/2018.
 */
@Service
public class ShoppingListService implements IShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private IShoppingListItemService iShoppingListItemService;

    @Override
    public List<ShoppingListDTO> getListsForUserByStatus(Long loggedUserId, boolean isArchived) {
        List<ShoppingList> list = shoppingListRepository.findByCreatorIdAndIsArchived(loggedUserId, isArchived);

        List<ShoppingListDTO> listDto = list.stream().map(l -> {
            ShoppingListDTO dto = new ShoppingListDTO(l);
            dto.setBoughtItems(iShoppingListItemService.getNumberOfPurchasedItems(l.getId()));
            dto.setNumberOfItems(iShoppingListItemService.getNumberOfItems(l.getId()));

            return dto;
        }).collect(Collectors.toList());


        return listDto;
    }

    @Override
    public ShoppingListDTO create(String listName, Long loggedUserId) {
        ShoppingListDTO shoppingListDTO = null;
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(listName);
        shoppingList.setCreatorId(loggedUserId);
        try {
            ShoppingList shoppingListNew = shoppingListRepository.save(shoppingList);
            shoppingListDTO = new ShoppingListDTO(shoppingListNew);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoppingListDTO;
    }

    @Override
    public ShoppingListDTO archive(long listId) {
        ShoppingListDTO shoppingListDTO = null;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingList.setIsArchived(true);
            shoppingList = shoppingListRepository.save(shoppingList);
            shoppingListDTO = new ShoppingListDTO(shoppingList);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return shoppingListDTO;
    }

    @Override
    public boolean makeSecret(Long listId, String password) {
        boolean success;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingList.setIsSecret(true);
            shoppingList.setAccessPassword(password);
            shoppingListRepository.save(shoppingList);
            success = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean makePublic(Long listId) {
        boolean success;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
                shoppingList.setIsSecret(false);
                shoppingList.setAccessPassword("");
                shoppingListRepository.save(shoppingList);
                success = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public void addReminder(Long listId, LocalDateTime reminder) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        shoppingList.setReminder(reminder);
        shoppingListRepository.save(shoppingList);
    }

    @Override
    public boolean restore(long listId) {
        boolean success;
        try{
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingList.setIsArchived(false);
            shoppingListRepository.save(shoppingList);
            success = true;
        }catch (NullPointerException e){
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean updateName(long listId, String listName) {
        boolean success = true;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingList.setListName(listName);
            shoppingListRepository.save(shoppingList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean shareList(Long listId, String sharedWith) {
        //check if user is registratet if not, send email invitation
        String curentlyLoggedUserName = "Milica";
        String subject = "MDJ - List shared";
        // will add url to
        String message = "Mr/s " + curentlyLoggedUserName + ", \n " + curentlyLoggedUserName + " has just shared shopping list with you. Click on notification. \n ";
//        emailService.sendEmail(subject, message, sharedWith);
        return true;
    }

    @Override
    public boolean saveListsFromLocalStorage(Long loggedUserId, List<ShoppingListDTO> listDTO) {
        boolean success = true;
        List<ShoppingList> list = listDTO.stream().map(l -> {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setListName(l.getListName());
            shoppingList.setCreatorId(loggedUserId);
            shoppingList.setIsSecret(l.getIsSecret());
            shoppingList.setAccessPassword(l.getAccessPassword());
            return shoppingList;
        }).collect(Collectors.toList());
        try {
            shoppingListRepository.saveAll(list);
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
    
    @Override
    public boolean updateLocation(long listId, Double latitude, Double longitude) {
        boolean success = true;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingList.setLatitude(latitude);
            shoppingList.setLongitude(longitude);
            shoppingListRepository.save(shoppingList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

	@Override
	public boolean deleteList(long listId) {
		boolean success = true;
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(NullPointerException::new);
            shoppingListRepository.delete(shoppingList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
	}

}
