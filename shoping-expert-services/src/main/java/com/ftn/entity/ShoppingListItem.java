package com.ftn.entity;

import com.ftn.dto.ShoppingListItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;


/**
 * Created by milca on 4/24/2018.
 */
@Entity
@Data
@NoArgsConstructor
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column
    private String note;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isPurchased;

    @Column
    private String imagePath;

    @Column
    private long categoryItemId;

    @Column
    private String categoryItemName;

    @Column
    private long shoppingListId;

    public ShoppingListItem(ShoppingListItemDTO shoppingListItemDTO){
        this.quantity = shoppingListItemDTO.getQuantity();
        this.price = shoppingListItemDTO.getPrice();
        this.note = shoppingListItemDTO.getNote();
        this.isPurchased = shoppingListItemDTO.getIsPurchased();
        this.imagePath = shoppingListItemDTO.getImagePath();
        this.categoryItemId = shoppingListItemDTO.getCategoryItemId();
        this.categoryItemName = shoppingListItemDTO.getCategoryItemName();
        this.shoppingListId = shoppingListItemDTO.getShoppingListId();
    }

    public ShoppingListItem(long categoryItemId, long shoppingListId, String categoryItemName){
        this.quantity = 1;
        this.price = 0.0;
        this.note = "";
        this.isPurchased = false;
        this.imagePath = "";
        this.categoryItemId = categoryItemId;
        this.categoryItemName = categoryItemName;
        this.shoppingListId = shoppingListId;
    }
}
