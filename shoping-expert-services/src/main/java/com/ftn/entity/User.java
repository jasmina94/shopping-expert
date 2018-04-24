package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
/**
 * Created by milca on 4/24/2018.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long Id;

    @Column
    private String FirstName;

    @Column
    private String LastName;

    @Column(nullable = false)
    private String Password;

    //email is at the same time username
    @Column(unique = true, nullable = false)
    @Email
    private String Email;
}
