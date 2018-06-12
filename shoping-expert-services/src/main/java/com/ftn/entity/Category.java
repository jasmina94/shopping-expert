package com.ftn.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

/**
 * Created by milca on 4/24/2018.
 */
@Data
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Column
    private String imagePath;
}
