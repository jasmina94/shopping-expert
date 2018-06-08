package com.ftn.service.serviceImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.dto.LoginDTO;
import com.ftn.dto.RegistrationDTO;
import com.ftn.dto.UserDTO;
import com.ftn.entity.ShoppingList;
import com.ftn.entity.User;
import com.ftn.repository.ShoppingListRepository;
import com.ftn.repository.UserRepository;
import com.ftn.service.IUserService;

/**
 * Created by Jasmina on 15/05/2018.
 */
@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShoppingListRepository shoppingListRepository;


    @Override
    public UserDTO register(RegistrationDTO registrationDTO) {
        UserDTO userDTO = null;
        boolean emailOk = checkEmail(registrationDTO.getEmail());
        if(emailOk) {
            try {
                User user = new User();
                user.merge(registrationDTO);
                if(registrationDTO.getRegisterType().equals("GOOGLE")) {
                    user.getInstancesOfUserDevices().add(registrationDTO.getDeviceInstance());
                }
                user = userRepository.save(user);
                userDTO = new UserDTO(user);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return userDTO;
    }

    @Override
    public UserDTO getById(int id) {
        UserDTO userDTO = null;
        long lId = ((long) id);
        try{
           User user = userRepository.findById(lId).orElseThrow(NullPointerException::new);
           userDTO = new UserDTO(user);
           return userDTO;
        }catch (NullPointerException e){
            return userDTO;
        }
    }

    @Override
    public User getByEmailRealUser(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public UserDTO getByEmail(String email) {
        UserDTO userDTO = null;
        User user = userRepository.findByEmail(email);
        if(user != null){
            userDTO = new UserDTO(user);
        }
        return userDTO;
    }

    @Override
    public UserDTO getByEmailAndPassword(String email, String password) {
        UserDTO userDTO = null;
        User user = userRepository.findByEmailAndPassword(email, password);
        if(user != null){
            userDTO = new UserDTO(user);
        }
        return userDTO;
    }

    @Override
    public boolean checkCredentials(LoginDTO loginDTO) {
        boolean credentialsOk = true;
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        UserDTO user = getByEmailAndPassword(email, password);
        if(user == null){
            return false;
        }
        addDeviceInstanceToUser(user.getId(), loginDTO.getDeviceInstance());
        return credentialsOk;
    }

    @Override
    public void removeDeviceInstanceFromUser(Long userId, String deviceInstance) {
        User realUser = userRepository.getOne(userId);
        realUser.getInstancesOfUserDevices().remove(deviceInstance);
        userRepository.save(realUser);
    }

    private void addDeviceInstanceToUser(Long userId, String deviceInstance) {
        User realUser = userRepository.getOne(userId);
        realUser.getInstancesOfUserDevices().add(deviceInstance);
        userRepository.save(realUser);
    }

    private boolean checkEmail(String email){
        boolean emailOk = true;
        User user = userRepository.findByEmail(email);
        if(user != null){
            emailOk = false;
        }
        return emailOk;
    }

	@Override
	public boolean saveShowNotifications(Long userId, Boolean showNotifications) {
		 boolean success;
	        try {
	            User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
	            user.setShowNotifications(showNotifications);            
	            userRepository.save(user);
	            success = true;
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	            success = false;
	        }
	        return success;
	}
	
	@Override
	public List<String> saveBlockedUsers(Long userId, String email, Boolean toBlock) {
		List<String> response = new ArrayList<String>();
	        try {
	            User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);  
	            User blockedUser = userRepository.findByEmail(email);
	            
	            if(blockedUser!=null){
	            	 List<String> blockedUsers = user.getBlockedUsers();
	 	            
	 	            if(toBlock){
	 	            	if(!blockedUsers.contains(email)){
	 		            	blockedUsers.add(email);
	 		            	///izbrisi serovane liste
	 		            	//pronadje sve liste kojima je kreator blokirani, a serovane su sa ulogovanim
	 		            	//izbrise ulogovanog iz liste serovanih, i blokiranog iz listi koje je ulogovani serovao sa njim
	 		            	List<ShoppingList> allShopingLists = shoppingListRepository.findAll();
	 		            	List<ShoppingList> sharedListsWhereBlockedIsCreator = (List<ShoppingList>) allShopingLists.stream()
	 		            			.filter(sl -> sl.getSharedWith().contains(user.getEmail()) && sl.getCreatorId() == blockedUser.getId()).collect(Collectors.toList());
	 		            	
	 		            	for(int i=0;i<sharedListsWhereBlockedIsCreator.size();i++){
	 		            		sharedListsWhereBlockedIsCreator.get(i).getSharedWith().remove(user.getEmail());
	 		            		shoppingListRepository.save(sharedListsWhereBlockedIsCreator.get(i));
	 		            	}	
	 		            	
	 		            	List<ShoppingList> sharedListsWhereLoggedIsCreator = (List<ShoppingList>) allShopingLists.stream()
	 		            			.filter(sl -> sl.getSharedWith().contains(blockedUser.getEmail()) && sl.getCreatorId() == user.getId()).collect(Collectors.toList());
	 		            	
	 		            	for(int i=0;i<sharedListsWhereLoggedIsCreator.size();i++){
	 		            		sharedListsWhereLoggedIsCreator.get(i).getSharedWith().remove(blockedUser.getEmail());
	 		            		shoppingListRepository.save(sharedListsWhereLoggedIsCreator.get(i));
	 		            	}	 
	 		            	
	 		            }
	 	            }else{
	 	            	if(blockedUsers.contains(email)){
	 		            	blockedUsers.remove(email);
	 		            }
	 	            }            
	 	            
	 	            user.setBlockedUsers(blockedUsers);
	 	            userRepository.save(user);
	 	            
	 	            response = blockedUsers;
	            }else{
	            	response = null;
	            }	           
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	            response = null;
	        }
	        return response;
	}

	@Override
	public List<String> getBlockedUsers(Long userId) {
		List<String> response = new ArrayList<String>();
        try {
            User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);            
            response = user.getBlockedUsers();

        } catch (NullPointerException e) {
            e.printStackTrace();
            response = null;
        }
        return response;
	}
}
