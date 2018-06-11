package com.ftn.dto;

import com.ftn.entity.ShoppingList;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by milca on 4/25/2018.
 */
@Data
@NoArgsConstructor
public class ShoppingListDTO {

    private Long id;

    private String creatorEmail;

    private String listName;

    private Boolean isSecret;

    private String accessPassword;

    private Integer boughtItems;

    private Integer numberOfItems;

    //emails of users that list is shared with
    private Set<String> sharedWith = new HashSet<>();

    private LocalDateTime reminder;

    //same for time and date of completion, need to investigate how reminders work

    private Double latitude;
    
    private Double longitude;

    public ShoppingListDTO(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.listName = shoppingList.getListName();
        this.isSecret = shoppingList.getIsSecret();
        this.sharedWith = shoppingList.getSharedWith();
        this.reminder = shoppingList.getReminder();
        this.accessPassword = shoppingList.getAccessPassword();
        this.latitude = shoppingList.getLatitude();
        this.longitude = shoppingList.getLongitude();
    }
}
