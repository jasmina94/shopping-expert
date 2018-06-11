package com.ftn.dto;

import com.ftn.entity.CategoryItem;

/**
 * Created by Jasmina on 11/06/2018.
 */
public class CategoryItemDTO {

    private long id;

    private String itemName;

    private long categoryId;


    public CategoryItemDTO(CategoryItem categoryItem){
        this.id = categoryItem.getId();
        this.itemName = categoryItem.getItemName();
        this.categoryId = categoryItem.getCategoryId();
    }
}
