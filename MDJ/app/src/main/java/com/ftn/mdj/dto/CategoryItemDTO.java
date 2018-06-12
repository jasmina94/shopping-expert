package com.ftn.mdj.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryItemDTO implements Serializable{

    private long id;

    private String itemName;

    private long categoryId;
}
