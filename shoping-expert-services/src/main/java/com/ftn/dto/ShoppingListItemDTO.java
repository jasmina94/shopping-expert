package com.ftn.dto;

import com.ftn.entity.ShoppingListItem;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Data
@NoArgsConstructor
public class ShoppingListItemDTO {

    private Long id;

    private Integer quantity;

    private Double price;

    private String note;

    private Boolean isPurchased;

    private String imagePath;

    private long categoryItemId;

    private String categoryItemName;

    private long shoppingListId;
    
    private String categoryName;


    public ShoppingListItemDTO(ShoppingListItem shoppingListItem, String categoryName){
        this.id = shoppingListItem.getId();
        this.quantity = shoppingListItem.getQuantity();
        this.price = shoppingListItem.getPrice();
        this.note = shoppingListItem.getNote();
        this.isPurchased = shoppingListItem.getIsPurchased();
        this.imagePath = shoppingListItem.getImagePath();
        this.categoryItemId = shoppingListItem.getCategoryItemId();
        this.categoryItemName = shoppingListItem.getCategoryItemName();
        this.shoppingListId = shoppingListItem.getShoppingListId();
        this.categoryName = categoryName;
    }

    public ShoppingListItemDTO(CategoryItemDTO categoryItemDTO, long shoppingListId){
        this.quantity = 1;
        this.price = 0.0;
        this.note = "";
        this.isPurchased = false;
        this.imagePath = "";
        this.categoryItemId = categoryItemDTO.getId();
        this.categoryItemName = categoryItemDTO.getItemName();
        this.shoppingListId = shoppingListId;
    }
}
