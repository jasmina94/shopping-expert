package com.ftn.dto;

import com.ftn.entity.CategoryItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Data
@NoArgsConstructor
public class CategoryItemDTO implements Serializable {

    private long id;

    private String itemName;

    private long categoryId;


    public CategoryItemDTO(CategoryItem categoryItem){
        this.id = categoryItem.getId();
        this.itemName = categoryItem.getItemName();
        this.categoryId = categoryItem.getCategoryId();
    }
}
