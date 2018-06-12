package com.ftn.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by milca on 4/24/2018.
 */
@Data
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Column
    private String imagePath;
}
