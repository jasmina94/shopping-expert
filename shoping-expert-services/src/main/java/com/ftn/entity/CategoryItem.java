package com.ftn.entity;

import com.ftn.dto.CategoryItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Entity
@Data
@NoArgsConstructor
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
