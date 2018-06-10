package com.ftn.service.serviceImplementation;

import com.ftn.dto.ShoppingListDTO;
import com.ftn.dto.UserDTO;
import com.ftn.entity.ShoppingList;
import com.ftn.entity.User;
import com.ftn.repository.ShoppingListRepository;
import com.ftn.service.IShoppingListItemService;
import com.ftn.service.IShoppingListService;
import com.ftn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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
    private IUserService iUserService;

    @Override
    public List<ShoppingListDTO> getListsForUserByStatus(Long loggedUserId, boolean isArchived) {
        UserDTO user = iUserService.getById(loggedUserId.intValue());
        List<ShoppingList> list = shoppingListRepository.findByCreatorIdAndIsArchived(loggedUserId, isArchived);

        List<ShoppingList> sharedLists = shoppingListRepository.findAll().stream().filter(l -> l.getSharedWith().contains(user.getEmail())).collect(Collectors.toList());

        list.addAll(sharedLists);

        List<ShoppingListDTO> listDto = list.stream().map(l -> {
            ShoppingListDTO dto = new ShoppingListDTO(l);
            dto.setBoughtItems(iShoppingListItemService.getNumberOfPurchasedItems(l.getId()));
            dto.setNumberOfItems(iShoppingListItemService.getNumberOfItems(l.getId()));
            dto.setCreatorEmail(iUserService.getById(l.getCreatorId().intValue()).getEmail());
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
    public List<String> shareList(Long listId, String sharedWith) {
        User user = iUserService.getByEmailRealUser(sharedWith);
        ShoppingList shoppingList = shoppingListRepository.findById(listId).get();

        if(user == null || user.getBlockedUsers().contains(sharedWith) || shoppingList.getCreatorId() == user.getId()) {
            return null;
        }

        shoppingList.getSharedWith().add(sharedWith);
        shoppingListRepository.save(shoppingList);
        List<String> response = user.getShowNotifications() ? new ArrayList<>(user.getInstancesOfUserDevices()): new ArrayList<>();
        return response;
    }

    @Override
    public boolean unShareList(Long listId, String unShareEmail) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId).get();
        if(shoppingList == null) {
            return false;
        }

        shoppingList.getSharedWith().remove(unShareEmail);

        shoppingListRepository.save(shoppingList);
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


    @Override
    public Map<String, Boolean> getFriendList(Long listId, Long userId) {
        ShoppingList shoppingList = shoppingListRepository.getOne(listId);
        UserDTO user = iUserService.getById(userId.intValue());
        Set<String> sharedWith = shoppingList.getSharedWith();

        List<ShoppingList> allShopingLists = shoppingListRepository.findAll();

        Set<String> friendEmails = allShopingLists.stream().filter(sl -> sl.getSharedWith().contains(user.getEmail()))
                .map(sl -> iUserService.getById(sl.getCreatorId().intValue()).getEmail()).collect(Collectors.toSet());

        sharedWith.forEach(sw -> {if(friendEmails.contains(sw)) {
            friendEmails.remove(sw);
        }});

        Map<String, Boolean> response = sharedWith.stream().collect(Collectors.toMap(Function.identity(), a -> Boolean.TRUE));

        Map<String, Boolean> withFalse = friendEmails.stream().collect(Collectors.toMap(Function.identity(),a -> Boolean.FALSE));

        response.putAll(withFalse);

        return response;
    }


}
