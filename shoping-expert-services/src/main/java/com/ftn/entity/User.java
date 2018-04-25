package com.ftn.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
@Component
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String password;

    //email is at the same time username
    @Column(unique = true, nullable = false)
    @Email
    private String email;
}
