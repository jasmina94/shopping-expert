package com.ftn.entity;

import com.ftn.dto.ShoppingListItemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by milca on 4/24/2018.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Component
public class ShoppingListItem {

    @Id
    @GeneratedValue
    @Column(name = "shopping_list_item_id")
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
    private long shoppingListId;

    public ShoppingListItem(ShoppingListItemDTO shoppingListItemDTO){
        this.quantity = shoppingListItemDTO.getQuantity();
        this.price = shoppingListItemDTO.getPrice();
        this.note = shoppingListItemDTO.getNote();
        this.isPurchased = shoppingListItemDTO.getIsPurchased();
        this.imagePath = shoppingListItemDTO.getImagePath();
        this.categoryItemId = shoppingListItemDTO.getCategoryItemId();
        this.shoppingListId = shoppingListItemDTO.getShoppingListId();
    }
}
