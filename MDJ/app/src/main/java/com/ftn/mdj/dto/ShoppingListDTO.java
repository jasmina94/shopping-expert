package com.ftn.mdj.dto;

import java.io.Serializable;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Jasmina on 17/04/2018.
 */
@Getter
@Setter
@NoArgsConstructor
public class ShoppingListDTO implements Serializable {

    private Long id;

    private String listName;

    private Boolean isSecret;

    private Integer boughtItems;

    private Integer numberOfItems;

    private String accessPassword;

    //emails of users that list is shared with
    private Set<String> sharedWith = new HashSet<>();

    private String reminder;

    public ShoppingListDTO(String name) {
        this.listName = name;
        this.isSecret = false;
        this.boughtItems = 0;
        this.numberOfItems = 0;
    }

    public ShoppingListDTO(Long id, String listName, Boolean isSecret, Integer boughtItems, Integer numberOfItems, Set<String> sharedWith, String reminder) {
        this.id = id;
        this.listName = listName;
        this.isSecret = isSecret;
        this.boughtItems = boughtItems;
        this.numberOfItems = numberOfItems;
        this.sharedWith = sharedWith;
        this.reminder = reminder;
    }
}
