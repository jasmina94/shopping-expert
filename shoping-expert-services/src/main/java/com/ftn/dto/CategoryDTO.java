package com.ftn.dto;

import com.ftn.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 11/06/2018.
 */
@Data
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    private String categoryName;

    private String imagePath;

    public CategoryDTO(Category category){
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.imagePath = category.getImagePath();
    }
}
