package com.ftn.mdj.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryItemDTO {

    private long id;

    private String itemName;

    private long categoryId;
}
