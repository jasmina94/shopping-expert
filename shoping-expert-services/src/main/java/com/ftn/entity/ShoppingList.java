package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by milca on 4/24/2018.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ShoppingList {

    @Id
    @GeneratedValue
    private Long Id;

    //User can have multiple same named lists
    @Column(nullable = false)
    private String ListName;

    @Column(columnDefinition = "Boolean default false")
    private Boolean IsSecret;

    //deletion of the list is only logical, so maybe if we have time we can have restore
    @Column(columnDefinition = "Boolean default false")
    private Boolean IsCompleted;

    private String AccessPassword;

    @Column(nullable = false)
    private Long CreatorId;

    @Column
    @ElementCollection(targetClass=Long.class)
    private Set<Long> SharedWith = new HashSet<>();

    //need to see how we will add location of shopping center, so I'll leave it for now

    //same for time and date of completion, need to investigate how reminders work
}
