package com.ftn.dto;

import com.ftn.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 15/05/2018.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;
    
    private Boolean showNotifications = true;
    
    private List<String> blockedUsers = new ArrayList<>();
    
    private Integer distanceForLocation = 100;

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.showNotifications = user.getShowNotifications();
        this.blockedUsers = user.getBlockedUsers();
        this.distanceForLocation = user.getDistanceForLocation();
    }

    public User constructEntity(){
        final User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setShowNotifications(showNotifications);
        user.setBlockedUsers(blockedUsers);
        user.setDistanceForLocation(distanceForLocation);
        return user;
    }
}
