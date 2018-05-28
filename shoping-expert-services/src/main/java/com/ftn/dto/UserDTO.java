package com.ftn.dto;

import com.ftn.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public User constructEntity(){
        final User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}
