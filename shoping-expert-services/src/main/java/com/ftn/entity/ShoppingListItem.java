package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by milca on 4/24/2018.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Component
public class ShoppingListItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    @Min(1)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    private String note;

    private Long categoryId;

    @Column
    @ElementCollection(targetClass=Long.class)
    private Set<Long> partOfShoppingLists = new HashSet<>();

    @Column(columnDefinition = "Boolean default false")
    private Boolean isPurchased;

    //items created by user are connected only to the list not the user, so item is existing only on list level

    //see how to add photo
}
