package com.ftn.dto;

import com.ftn.entity.ShoppingList;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

/**
 * Created by milca on 4/25/2018.
 */
@Getter
@Setter
public class ShoppingListShowDto {

    private Long id;

    private String listName;

    private Boolean isSecret;

    private Integer boughtItems;

    private Integer numberOfItems;

    //emails of users that list is shared with
    private Set<String> sharedWith = new HashSet<>();

    private LocalDateTime reminder;
    //same for time and date of completion, need to investigate how reminders work

    private Double latitude;
    
    private Double longitude;

    public ShoppingListShowDto() {}

    public ShoppingListShowDto(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.listName = shoppingList.getListName();
        this.isSecret = shoppingList.getIsSecret();
        this.sharedWith = shoppingList.getSharedWith();
        this.reminder = shoppingList.getReminder();
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
