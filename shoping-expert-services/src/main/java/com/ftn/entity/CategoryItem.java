package com.ftn.entity;

import com.ftn.dto.CategoryItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Data
@NoArgsConstructor
@Entity
public class CategoryItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private long categoryId;

    public CategoryItem(CategoryItemDTO categoryItemDTO){
        this.id = null;
        this.itemName = categoryItemDTO.getItemName();
        this.categoryId = categoryItemDTO.getCategoryId();
    }
}
