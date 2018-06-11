package com.ftn.mdj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    private long shoppingListId;

    private String categoryItemName;
}
