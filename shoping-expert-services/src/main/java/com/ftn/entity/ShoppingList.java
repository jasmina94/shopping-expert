package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class  ShoppingList {

    @Id
    @GeneratedValue
    private Long id;

    //User can have multiple same named lists
    @Column(nullable = false)
    private String listName;

    @Column(columnDefinition = "Boolean default false")
    private Boolean isSecret = false;


    @Column(columnDefinition = "Boolean default false")
    private Boolean isArchived = false;

    private String accessPassword;

    @Column(nullable = false)
    private Long creatorId;

    @Column
    @ElementCollection(targetClass=String.class)
    private Set<String> sharedWith = new HashSet<>();

    //need to see how we will add location of shopping center, so I'll leave it for now
    @Column
    private LocalDateTime reminder;
    //same for time and date of completion, need to investigate how reminders work
    
    @Column
    private Double latitude;
    
    @Column
    private Double longitude;

    public ShoppingList(String listName, long creatorId){
        this.creatorId = creatorId;
        this.listName = listName;
        this.accessPassword = "";
        this.reminder = null;
        this.longitude = 0.0;
        this.latitude = 0.0;
    }
}
