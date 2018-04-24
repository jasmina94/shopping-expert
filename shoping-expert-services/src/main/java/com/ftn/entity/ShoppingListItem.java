package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ShoppingListItem {

    @Id
    @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String ItemName;

    @Column(nullable = false)
    @Min(1)
    private Integer Quantity;

    @Column(nullable = false)
    private Double Price;

    private String Note;

    private Long CategoryId;

    @Column
    @ElementCollection(targetClass=Long.class)
    private Set<Long> PartOfShoppingLists = new HashSet<>();

    @Column(columnDefinition = "Boolean default false")
    private Boolean IsPurchased;

    //items created by user are connected only to the list not the user, so item is existing only on list level

    //see how to add photo
}
