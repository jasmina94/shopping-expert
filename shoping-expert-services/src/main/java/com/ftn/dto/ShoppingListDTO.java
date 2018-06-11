package com.ftn.dto;

import com.ftn.entity.ShoppingList;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Set<String> sharedWith = new HashSet<>();

    private String date;

    private String time;

    private Double latitude;
    
    private Double longitude;

    public ShoppingListDTO(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.listName = shoppingList.getListName();
        this.isSecret = shoppingList.getIsSecret();
        this.sharedWith = shoppingList.getSharedWith();
        this.date = shoppingList.getDate();
        this.time = shoppingList.getTime();
        this.accessPassword = shoppingList.getAccessPassword();
        this.latitude = shoppingList.getLatitude();
        this.longitude = shoppingList.getLongitude();
    }
}
