package com.ftn.mdj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    private String categoryName;

    private String imagePath;
}
