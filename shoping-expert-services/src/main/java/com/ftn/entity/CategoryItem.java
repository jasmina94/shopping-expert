package com.ftn.entity;

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
    @Column(name = "category_item_id")
    private long id;

    @Column(nullable = false)
    private String itemName;

    private long categoryId;
}
